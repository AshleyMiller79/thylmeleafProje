package thymeleaf3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import thymeleaf3.model.Employee;
import thymeleaf3.repository.EmployeeRepository;

@Controller
public class EmployeeController {

	
	private EmployeeRepository empRepo;
	@Autowired
	public EmployeeController(EmployeeRepository employeeRepository) {
		empRepo=employeeRepository;
	}
	
	@GetMapping({"/list"})//1)ilk açılan sayfa list-employees, eğer önceden db e girilmiş veriler varsa dolu gelir,
	public ModelAndView getAllEmployees() {
	//ModelAndView=Web MVC çerçevesinde hem Model hem de Görünüm (view) için tutucu. Bunların tamamen 
	//farklı olduğunu unutmayın. Bu sınıf, yalnızca bir denetleyicinin hem modeli hem de 
	//görünümü tek bir dönüş değerinde döndürmesini mümkün kılmak için her ikisini de tutar.
		//bir obje oluştur ModelAndView den, içinde list-employee olsun, obje ekle,
		//backendden verileri getirsin
		ModelAndView mav = new ModelAndView("list-employee","calisanlar",empRepo.findAll());
		//ModelAndView mav = new ModelAndView("list-employee");
		//mav.addObject("calisanlar", empRepo.findAll());//list-employees class ına, kullanıcının girdiği listedeki verileri employees ismiyle yolluyoruz
		return mav;
		//calisanlar :bütün liste
	}
	//burası react taki ekleme sayfasını aç diye çalışıyor
		@GetMapping("/addEmployeeForm")//4)
		public ModelAndView addEmployeeForm() {//list-employees (üstteki) class ındaki, add employee butonuna basınca burası tetiklendi
			ModelAndView mav = new ModelAndView("add-employee-form","employee",new Employee());
			//veriler kullanıcı tarafından add-employee-form class ında  girilecek (5 orada) ve o sayfadaki save button a basınca....
//			Employee newKisi = new Employee();
//			mav.addObject("employee", newKisi);
			//model class ından obje oluşturup, kullanıcının girdiği veriler, save butonuna bastığınız zaman veritabanına kayda hazır hale geldi,
			//veriler buradan giden employee objesine gömüldü
			return mav;
		}
		
		@PostMapping("/saveEmployee")//
		public String saveEmployee(@ModelAttribute Employee employee) {
			
			empRepo.save(employee);//6)add-employee-form sayfasından gelen veriler save oldu ve list komutu tetiklendi ve en üstteki list-employees sayfasına geçildi
			return "redirect:/list";//list komutunun gittiği yere yönlendir(en üstte)
		}
		//@ModelAttribute: biz thymeleaf ile burada frontend oluşturduk ve verileri ayrı ayrı ele aldık
		// name, email ayrı ayrı gelir, o zaman ModelAttribute kullanabiliriz, veriler model class ındaki gibi gelir çünkü
		//biz frontendde verileri ayrıştırmazsak veriler toplu halde json formatında gelir ve requestbody kullanmamız gerekir  
//		@ModelAttribute:
//			Tüm form parametrelerinin model nesnesinde old gibi bağlanmasını istiyoruz. Bu, bir argüman düzeyinde uygulanır.
//			 Tüm denetleyici yöntemleri için ortak bir başlık istiyorsak, yöntem düzeyinde @ModelAttribute kullanırız
	//
//			@RequestBody
//			POST veya PUT isteği olması durumunda birde JSON biçiminde gelen istek verilerimiz var ve model nesnesini başlatmak için 
		//sunucu tarafında tüm isteği döndürmek istiyoruz.
//	      @modelAttribute form parametrelerini yakalamak
//			için @RequestBody tüm isteği yakalamak için
		
		@GetMapping("/showUpdateForm")
		public ModelAndView showUpdateForm(@RequestParam Long employeeId) {
			ModelAndView mav = new ModelAndView("add-employee-form","employee",empRepo.findById(employeeId).get());
//			Employee employee = empRepo.findById(employeeId).get();//jpa ile id siyle employee nin bilgilerini getir
//			mav.addObject("employee", employee);//mavi employee ile add-employee-form sayfasına gönder
			return mav;
		}
	
	@GetMapping("/deleteEmployee")
	public String deleteEmployee(@RequestParam  Long employeeId) {
	empRepo.deleteById(employeeId);
	
	return "redirect:/list";}
	
	
	
	
}

