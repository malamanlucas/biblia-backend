package br.com.biblia;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class Main {

	public static void main(String[] args) {
		
		List<Integer> s = Lists.newArrayList(4,7,1);
			
		System.out.println(s.stream().filter(f -> false).collect(Collectors.toList()));
		
	}
	
}
