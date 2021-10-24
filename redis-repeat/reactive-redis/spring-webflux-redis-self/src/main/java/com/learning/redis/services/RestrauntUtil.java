package com.learning.redis.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.redis.beans.Restraunt;

public class RestrauntUtil {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static final String RESTRAUNT_JSON_FILE_PATH = "restraunt.json";

	public static List<Restraunt> readFile() {
		InputStream resourceAsStream = RestrauntUtil.class.getClassLoader()
				.getResourceAsStream(RESTRAUNT_JSON_FILE_PATH);
		try {
			List<Restraunt> readValue = MAPPER.readValue(resourceAsStream, new TypeReference<List<Restraunt>>() {
			});

			// System.out.println("size "+readValue.size());
			return readValue;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
