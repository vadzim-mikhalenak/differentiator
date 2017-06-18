package com.waes.differentiator.domain.service;

import com.waes.differentiator.DifferentiatorApplication;
import com.waes.differentiator.TestConfiguration;
import com.waes.differentiator.domain.model.CompareResult;
import com.waes.differentiator.domain.model.DocumentEntity;
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
		Assert.assertEquals(CompareResult.Status.EQUAL, compareResult.getStatus());
	}

	@Test
	public void testDocumentWithNullForLeftOnly() {
		documentService.updateDocumentWithLeft(1L, null);
		byte[] testData = {0, 1, 0, 1};
		documentService.updateDocumentWithRight(1L, testData);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(CompareResult.Status.DIFFERENT_SIZE, compareResult.getStatus());
	}

	@Test
	public void testDocumentWithNullForRightOnly() {
		documentService.updateDocumentWithRight(1L, null);
		byte[] testData = {0, 1, 0, 1};
		documentService.updateDocumentWithLeft(1L, testData);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(CompareResult.Status.DIFFERENT_SIZE, compareResult.getStatus());
	}

	@Test
	public void testDocumentWithDifferentSizeForLeftAndRight() {
		byte[] testData = {0, 1, 0, 1};
		documentService.updateDocumentWithLeft(1L, testData);
		byte[] testData2 = {0, 1, 0, 1, 0};
		documentService.updateDocumentWithRight(1L, testData2);

		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		DocumentEntity documentEntity = documentService.readDocument(1L);
		Assert.assertEquals(4, documentEntity.getLeft().length);
		Assert.assertEquals(5, documentEntity.getRight().length);
		Assert.assertEquals(CompareResult.Status.DIFFERENT_SIZE, compareResult.getStatus());
	}

	@Test
	public void testDocumentWithDifferentContent() {
		byte[] testData = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		documentService.updateDocumentWithRight(1L, testData);
		byte[] testData2 = {0, 1, 1, 1, 0, 0, 0, 1, 0, 0};
		documentService.updateDocumentWithLeft(1L, testData2);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(CompareResult.Status.DIFFERENT_CONTENT, compareResult.getStatus());
		Assert.assertNotNull(compareResult.getDiffs());
		Assert.assertEquals(3, compareResult.getDiffs().size());

		Assert.assertEquals(0, (int) compareResult.getDiffs().get(0).getOffset());
		Assert.assertEquals(1, (int) compareResult.getDiffs().get(0).getSize());

		Assert.assertEquals(4, (int) compareResult.getDiffs().get(1).getOffset());
		Assert.assertEquals(3, (int) compareResult.getDiffs().get(1).getSize());

		Assert.assertEquals(8, (int) compareResult.getDiffs().get(2).getOffset());
		Assert.assertEquals(2, (int) compareResult.getDiffs().get(2).getSize());
	}

	@Test
	public void testDocumentWithAllDifferentContent() {
		byte[] testData = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		documentService.updateDocumentWithRight(1L, testData);
		byte[] testData2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		documentService.updateDocumentWithLeft(1L, testData2);
		CompareResult compareResult = documentService.diffDocument(1L);
		Assert.assertNotNull(compareResult);
		Assert.assertEquals(CompareResult.Status.DIFFERENT_CONTENT, compareResult.getStatus());
		Assert.assertNotNull(compareResult.getDiffs());
		Assert.assertEquals(1, compareResult.getDiffs().size());

		Assert.assertEquals(0, (int) compareResult.getDiffs().get(0).getOffset());
		Assert.assertEquals(10, (int) compareResult.getDiffs().get(0).getSize());

	}

}
