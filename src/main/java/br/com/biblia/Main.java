package br.com.biblia;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class Main {

	public static void main(String[] args) {
		
		
		List<Integer> a = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		
		System.out.println(a.stream().filter(i -> i >= 3 && i <= 5).collect(Collectors.toList()));
		
		
	}
	
}
