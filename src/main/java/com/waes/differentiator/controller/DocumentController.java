package com.waes.differentiator.controller;

import com.waes.differentiator.client.converter.ClientConverter;
import com.waes.differentiator.client.model.DocumentPartRequest;
import com.waes.differentiator.client.model.ErrorResponse;
import com.waes.differentiator.controller.exception.InvalidBase64PayloadException;
import com.waes.differentiator.domain.model.CompareResult;
import com.waes.differentiator.domain.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * @author Vadzim Mikhalenak.
 */
@RestController
@RequestMapping("v1/diff")
public class DocumentController {

	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	private final DocumentService documentService;

	@Autowired
	public DocumentController(DocumentService documentService) {
		this.documentService = documentService;
	}

	@RequestMapping(value = "{id}/left", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void setLeftPart(@PathVariable("id") Long id, @RequestBody DocumentPartRequest documentPart) {
		logger.info("#{}: update left part", id);

		documentService.updateDocumentWithLeft(id, decodeBase64Payload(documentPart.getContent()));
	}

	@RequestMapping(value = "{id}/right", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void setRightPart(@PathVariable("id") Long id, @RequestBody DocumentPartRequest documentPart) {
		logger.info("#{}: update right part", id);

		documentService.updateDocumentWithRight(id, decodeBase64Payload(documentPart.getContent()));
	}

	private byte[] decodeBase64Payload(String base64Payload) {
		if (base64Payload == null) {
			return null;
		}
		try {
			return Base64.getDecoder().decode(base64Payload.trim());
		} catch (IllegalArgumentException e) {
			throw new InvalidBase64PayloadException(base64Payload);
		}
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getDiff(@PathVariable("id") Long id) {
		logger.info("#{}: get diff", id);
		CompareResult compareResult = documentService.diffDocument(id);
		if (compareResult == null) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse("There is no document with id: " + id));
		}
		return ResponseEntity.ok(ClientConverter.convert(compareResult));
	}

	@ExceptionHandler(InvalidBase64PayloadException.class)
	public ResponseEntity handleError(HttpServletRequest req, InvalidBase64PayloadException ex) {
		logger.error("Request: " + req.getRequestURL() + " raised " + ex);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("Invalid format of payload: " + ex.getSourceBase64Payload()));
	}
}
