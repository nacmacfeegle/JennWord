package world.domain.jenn;


public class JennTest {
	public static void main(String args[]) {
		
		Jenn j = new Jenn(new JennDomain(), 31);
		j.measure();
		System.out.println("Quality: " +j.quality());
		System.out.println("Typicality: " +j.typicality());
	
		String exportedFile = j.export("out");
		System.out.println("Exported file to location:  "+exportedFile);
	}
}
