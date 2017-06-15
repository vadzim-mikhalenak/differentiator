package com.waes.differentiator.client.converter;

import com.waes.differentiator.client.model.CompareResultResponse;
import com.waes.differentiator.domain.model.CompareResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Vadzim Mikhalenak.
 */
public class ClientConverterTests {

	@Test
	public void testStatusIsNull() {

		CompareResult compareResult = new CompareResult(1L);

		CompareResultResponse actual = ClientConverter.convert(compareResult);
		Assert.assertNull(actual.getStatus());
		Assert.assertNull(actual.getDiffs());
	}

	@Test
	public void testStatusIsEqual() {

		CompareResult compareResult = new CompareResult(1L);
		compareResult.setStatus(CompareResult.Status.EQUAL);

		CompareResultResponse actual = ClientConverter.convert(compareResult);
		Assert.assertEquals(actual.getStatus(), CompareResultResponse.Status.EQUAL);
		Assert.assertNull(actual.getDiffs());
	}

	@Test
	public void testStatusIsDifferentSize() {

		CompareResult compareResult = new CompareResult(1L);
		compareResult.setStatus(CompareResult.Status.DIFFERENT_SIZE);

		CompareResultResponse actual = ClientConverter.convert(compareResult);
		Assert.assertEquals(actual.getStatus(), CompareResultResponse.Status.DIFFERENT_SIZE);
		Assert.assertNull(actual.getDiffs());
	}

	@Test
	public void testStatusIsDifferentContent() {

		CompareResult compareResult = new CompareResult(1L);
		compareResult.setStatus(CompareResult.Status.DIFFERENT_CONTENT);

		List<CompareResult.DiffPart> diffs = new LinkedList<>();
		diffs.add(new CompareResult.DiffPart(0, 2));
		diffs.add(new CompareResult.DiffPart(3, 4));
		compareResult.setDiffs(diffs);

		CompareResultResponse actual = ClientConverter.convert(compareResult);
		Assert.assertEquals(actual.getStatus(), CompareResultResponse.Status.DIFFERENT_CONTENT);
		Assert.assertNotNull(actual.getDiffs());
		Assert.assertEquals(actual.getDiffs().size(), 2);

		Assert.assertTrue(actual.getDiffs().get(0).getOffset() == 0);
		Assert.assertTrue(actual.getDiffs().get(0).getSize() == 2);

		Assert.assertTrue(actual.getDiffs().get(1).getOffset() == 3);
		Assert.assertTrue(actual.getDiffs().get(1).getSize() == 4);

	}

}
