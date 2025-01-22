package soa.lab2.orgmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class OrgManagerApplicationTests {

	@Test
	void test() {
		Integer a = 127;
		Integer b = 127;
		System.out.println(a==b);

		Integer a1 = 128;
		Integer b1 = 128;
		System.out.println(a1==b1);
	}
}
