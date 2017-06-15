package com.waes.differentiator.client.converter;

import com.waes.differentiator.client.model.CompareResultResponse;
import com.waes.differentiator.client.model.DiffPartResponse;
import com.waes.differentiator.domain.model.CompareResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts internal server representation of entities to client specific one.
 *
 * @author Vadzim Mikhalenak.
 */
public final class ClientConverter {

	private ClientConverter() {
	}

	public static CompareResultResponse convert(CompareResult domain) {
		CompareResultResponse result = new CompareResultResponse();
		result.setStatus(convert(domain.getStatus()));
		result.setDiffs(convert(domain.getDiffs()));

		return result;
	}

	private static List<DiffPartResponse> convert(List<CompareResult.DiffPart> diffs) {
		if (diffs == null || diffs.isEmpty()) {
			return null;
		}

		return diffs.stream()
				.map(diffPart -> new DiffPartResponse(diffPart.getOffset(), diffPart.getSize()))
				.collect(Collectors.toList());
	}

	private static CompareResultResponse.Status convert(CompareResult.Status status) {
		if (status == null) {
			return null;
		}
		switch (status) {
			case DIFFERENT_CONTENT:
				return CompareResultResponse.Status.DIFFERENT_CONTENT;
			case DIFFERENT_SIZE:
				return CompareResultResponse.Status.DIFFERENT_SIZE;
			case EQUAL:
				return CompareResultResponse.Status.EQUAL;
			default:
				throw new IllegalArgumentException("cannot convert server specific Status value "
						+ status.name()
						+ " to client specific one.");

		}

	}
}
