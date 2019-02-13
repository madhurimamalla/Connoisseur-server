package com.github.madhurimamalla.connoisseur.server.similarity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaccardSimilarity {

	private static final Logger LOG = LoggerFactory.getLogger(JaccardSimilarity.class);

	public static <T> float compute(Set<T> set1, Set<T> set2) {
		Collection<T> similar = new HashSet<T>(set1);
		similar.retainAll(set2);
		float intersectionLength = similar.size();
		float unionLength = set1.size() + set2.size() - intersectionLength;
		return intersectionLength / unionLength;
	}

}