package com.github.madhurimamalla.connoisseur.server.similarity;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.madhurimamalla.connoisseur.server.similarity.JaccardSimilarity;

@RunWith(SpringRunner.class)
@Configuration
@PropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SimilarityTests {

	private static final Logger LOG = LoggerFactory.getLogger(SimilarityTests.class);

	@Test
	public void testSimilarityMetric() {

		assertTrue(JaccardSimilarity.compute(new HashSet<String>(Arrays.asList("ABC", "DEF", "XYZ")),
				new HashSet<String>(Arrays.asList("DEF", "XYZ", "MNO"))) == 0.5f);
	}

}
