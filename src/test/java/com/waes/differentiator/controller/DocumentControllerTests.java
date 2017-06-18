package com.waes.differentiator.controller;

import com.waes.differentiator.DifferentiatorApplication;
import com.waes.differentiator.TestConfiguration;
import com.waes.differentiator.client.model.CompareResultResponse;
import com.waes.differentiator.client.model.DocumentPartRequest;
import com.waes.differentiator.domain.model.DocumentEntity;
import com.waes.differentiator.domain.service.DocumentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Vadzim Mikhalenak.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DifferentiatorApplication.class, TestConfiguration.class})
@WebAppConfiguration
public class DocumentControllerTests {

	private final static MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private DocumentService documentService;

	@Before
	public void setup() {
		mockMvc = webAppContextSetup(webApplicationContext).build();
		documentService.deleteAllDocuments();
	}

	@Test
	public void testSetLeftPartWithNullContent() throws Exception {
		mockMvc.perform(createPost(1, DocumentPart.LEFT)
				.content(json(new DocumentPartRequest()))
				.contentType(contentType))
				.andExpect(status().isOk());

		DocumentEntity documentEntity = documentService.readDocument(1L);
		Assert.assertNotNull(documentEntity);
		Assert.assertNull(documentEntity.getLeft());
	}

	@Test
	public void testSetLeftPartWithEmptyContent() throws Exception {
		mockMvc.perform(createPost(1, DocumentPart.LEFT)
				.content(json(new DocumentPartRequest("")))
				.contentType(contentType))
				.andExpect(status().isOk());

		DocumentEntity documentEntity = documentService.readDocument(1L);
		Assert.assertNotNull(documentEntity);
		Assert.assertNotNull(documentEntity.getLeft());
	}

	@Test
	public void testSetRightPartWithNullContent() throws Exception {
		mockMvc.perform(createPost(1, DocumentPart.RIGHT)
				.content(json(new DocumentPartRequest()))
				.contentType(contentType))
				.andExpect(status().isOk());

		DocumentEntity documentEntity = documentService.readDocument(1L);
		Assert.assertNotNull(documentEntity);
		Assert.assertNull(documentEntity.getRight());
	}

	@Test
	public void testSetRightPartWithEmptyContent() throws Exception {
		mockMvc.perform(createPost(1, DocumentPart.RIGHT)
				.content(json(new DocumentPartRequest("")))
				.contentType(contentType))
				.andExpect(status().isOk());

		DocumentEntity documentEntity = documentService.readDocument(1L);
		Assert.assertNotNull(documentEntity);
		Assert.assertNotNull(documentEntity.getRight());
	}

	/**
	 * Example of some kind of Integration test.
	 */
	@Test
	public void testContentOfDifferentSize() throws Exception {
		mockMvc.perform(createPost(1, DocumentPart.RIGHT)
				.content(json(new DocumentPartRequest("ABC")))
				.contentType(contentType))
				.andExpect(status().isOk());

		mockMvc.perform(createPost(1, DocumentPart.LEFT)
				.content(json(new DocumentPartRequest("ABCD")))
				.contentType(contentType))
				.andExpect(status().isOk());

		CompareResultResponse compareResultResponse = new CompareResultResponse();
		compareResultResponse.setStatus(CompareResultResponse.Status.DIFFERENT_SIZE);

		mockMvc.perform(createGet(1))
				.andExpect(status().isOk())
				.andExpect(content().json(json(compareResultResponse)));
	}

	@Test
	public void testInvalidBase64Payload() throws Exception {
		mockMvc.perform(createPost(1, DocumentPart.RIGHT)
				.content(json(new DocumentPartRequest("ABC A")))
				.contentType(contentType))
				.andExpect(status().isBadRequest());
	}

	private MockHttpServletRequestBuilder createPost(int documentId, DocumentPart documentPart) {
		return post("/v1/diff/" + documentId + documentPart.urlPart);
	}

	private MockHttpServletRequestBuilder createGet(int documentId) {
		return get("/v1/diff/" + documentId);
	}

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		mappingJackson2HttpMessageConverter = Arrays.stream(converters)
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny()
				.orElse(null);

		assertNotNull("the JSON message converter must not be null",
				mappingJackson2HttpMessageConverter);
	}

	private String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

	private enum DocumentPart {
		LEFT("/left"), RIGHT("/right");

		final String urlPart;

		DocumentPart(String urlPart) {
			this.urlPart = urlPart;
		}
	}

}
