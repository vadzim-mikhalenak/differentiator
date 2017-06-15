package com.waes.differentiator.domain.service;

import com.waes.differentiator.DifferentiatorApplication;
import com.waes.differentiator.TestConfiguration;
import com.waes.differentiator.domain.model.CompareResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Vadzim Mikhalenak.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DifferentiatorApplication.class, TestConfiguration.class})
public class DocumentServiceTests {

	@Autowired
	private DocumentService documentService;

	@Before
	public void setup() {
		documentService.deleteAllDocuments();
	}

	@Test
	public void testNoDocumentExists() {
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNull(compareResult);
	}

	@Test
	public void testDocumentWithNullForLeftAndRight() {
		documentService.updateDocumentWithLeft(1L, null);
		documentService.updateDocumentWithRight(1L, null);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(compareResult.getStatus(), CompareResult.Status.EQUAL);
	}

	@Test
	public void testDocumentWithNullForLeftOnly() {
		documentService.updateDocumentWithLeft(1L, null);
		byte[] testData = {0, 1, 0, 1};
		documentService.updateDocumentWithRight(1L, testData);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(compareResult.getStatus(), CompareResult.Status.DIFFERENT_SIZE);
	}

	@Test
	public void testDocumentWithNullForRightOnly() {
		documentService.updateDocumentWithRight(1L, null);
		byte[] testData = {0, 1, 0, 1};
		documentService.updateDocumentWithLeft(1L, testData);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(compareResult.getStatus(), CompareResult.Status.DIFFERENT_SIZE);
	}

	@Test
	public void testDocumentWithDifferentSizeForLeftAndRight() {
		byte[] testData = {0, 1, 0, 1};
		documentService.updateDocumentWithRight(1L, testData);
		byte[] testData2 = {0, 1, 0, 1, 0};
		documentService.updateDocumentWithLeft(1L, testData2);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(compareResult.getStatus(), CompareResult.Status.DIFFERENT_SIZE);
	}

	@Test
	public void testDocumentWithDifferentContent() {
		byte[] testData = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		documentService.updateDocumentWithRight(1L, testData);
		byte[] testData2 = {0, 1, 1, 1, 0, 0, 0, 1, 0, 0};
		documentService.updateDocumentWithLeft(1L, testData2);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(compareResult.getStatus(), CompareResult.Status.DIFFERENT_CONTENT);
	}

}
