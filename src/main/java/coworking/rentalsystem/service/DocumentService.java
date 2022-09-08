package coworking.rentalsystem.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import coworking.rentalsystem.model.entity.RentApplication;
import org.librepdf.openpdf.fonts.Liberation;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

public class DocumentService implements IDocumentService{

    @Override
    public void export(RentApplication rentApplication, HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font boldFontLg = Liberation.SANS_BOLD.create();
        boldFontLg.setSize(18);
        Font boldFont = Liberation.SANS_BOLD.create();
        Font font = Liberation.SANS.create();

        Paragraph p = new Paragraph("Договір оренди робочого місця №" + rentApplication.getLeaseAgreement(), boldFontLg);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        document.add(new Paragraph("м.Харків", boldFont));
        document.add(new Paragraph("Товариство з обмеженою відповідальністю \"Ворк Хаб\" (надалі іменується " +
                "\"Орендодавець\"), з однієї сторони, та Громадянин України "+ rentApplication.getUser().getPersonalData().getLastName() + " "  +
                rentApplication.getUser().getPersonalData().getFirstName() + " " + rentApplication.getUser().getPersonalData().getMiddleName() +
                ", паспорт "+ rentApplication.getUser().getPersonalData().getPassportId()+", виданий " + rentApplication.getUser().getPersonalData().getAuthority() +
                ", ІНН " + rentApplication.getUser().getPersonalData().getITN() +
                " (надалі іменується \"Орендар\"), з іншої сторони, в подальшому разом іменуються \"Сторони\", а кожна окремо - " +
                "\"Сторона\" уклали цей Договір оренди (надалі іменується \"Договір\") про таке:\n", font));
        document.add(new Paragraph("1. ПРЕДМЕТ ДОГОВОРУ.", boldFont));
        document.add(new Paragraph("1.1. Орендодавець надає Орендареві у тимчасове володіння та користування за плату робоче місце, розташоване за адресою: м. Харків, вул. Благовіщенська, 1.\n" +
                "1.2. Робоче місце обладнане та оснащене: столом, стулом.\n" +
                "1.3. Загальна площа приміщення, у якому знаходиться робоче місце: " + rentApplication.getPlace().getRoom().getArea() +" кв.м., площа, що орендується: "+ rentApplication.getPlace().getArea()+" кв.м.\n"+
                "1.4. Робоче місце використовується Орендарем лише для професійної діяльності.\n" +
                "1.5 Час доступу Орендаря до робочого місця: з 8.00 до 20.00 години щодня.\n" +
                "1.6 Термін оренди: з "+ new SimpleDateFormat("dd/MM/yyyy").format(rentApplication.getRentStart()) +" по " +  new SimpleDateFormat("dd/MM/yyyy").format(rentApplication.getRentEnd()), font));
        document.add(new Paragraph("2.ОПЛАТА ПОСЛУГ ОРЕНДИ", boldFont));
        document.add(new Paragraph("2.1. Сторони дійшли згоди, що орендна плата за користування одного робочого місця становить "+ rentApplication.getRentAmount() +" грн. без ПДВ. \n" +
                "2.2. В суму орендної плати включена вартість користування робочим місцем, вартість користування безлімітним доступом до мережі Інтернет, вартість користування комунальним та експлуатаційними послугами (електропостачання, опалення, каналізація та інше).\n" +
                "2.3. У разі зміни розміру орендної плати Сторони погоджують нові цінові розрахунки шляхом підписання відповідної Додаткової угоди до цього Договору, що вступає в дію з дати її підписання.\n", font));
        document.newPage();
        writer.setPageEmpty( false );

        Paragraph p2 = new Paragraph("Акт приймання-передачі", boldFontLg);
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph p3 = new Paragraph("на основі договору оренди № " + rentApplication.getLeaseAgreement(), boldFont);
        p3.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p2);
        document.add(p3);
        document.add(new Paragraph("м.Харків",boldFont));
        document.add(new Paragraph("Товариство з обмеженою відповідальністю \"Ворк Хаб\", іменоване надалі «Орендодавець», з одного боку, та" +
                "Громадянин України "+ rentApplication.getUser().getPersonalData().getLastName() + " "  +
                rentApplication.getUser().getPersonalData().getFirstName() + " "
                + rentApplication.getUser().getPersonalData().getMiddleName()
                + ", іменований надалі «Орендар», з іншого боку, спільно іменовані Сторонами, склали цей Акт про передачу в тимчасове користування Орендодавцем Орендареві за плату робоче місце розташоване за адресою: м. Харків, вул. Благовіщенська, 1, укомлектоване меблями:\n", font));
        switch (rentApplication.getPlace().getRoom().getRoomType().getName()){
            case "open space": document.add(new Paragraph("\tстіл (1 шт.), стул (1 шт.)", font)); break;
            case "meeting room": document.add(new Paragraph("\tстіл (1 шт.), стул (6 шт.), проектор (1 шт.), екран (1 шт.)", font));break;
            case "skype room": document.add(new Paragraph("\tстіл (1 шт.), стул (1 шт.), ноутбук (1 шт.)", font));break;
        }
        document.add(new Paragraph("Технічний стан об'єкту оренди (включаючи меблі)  задовільний і дозволяє використовувати їх відповідно до предмету Договору.\n" +
                "Цей Акт складений у двох екземплярах, по одному для кожної із Сторін.\n" +
                "В разі нанесення Орендарем матеріального збитку майну Орендодавця, Сторонами складається Акт про нанесення матеріального збитку, відповідно до якого відбувається відшкодування матеріального збитку.\n", font));
        document.add(new Paragraph("Майно передав: Товариство з обмеженою відповідальністю \"Ворк Хаб\"", boldFont));
        document.add(new Paragraph("Майно прийняв: " + rentApplication.getUser().getPersonalData().getLastName() + " "  +
                rentApplication.getUser().getPersonalData().getFirstName() + " " + rentApplication.getUser().getPersonalData().getMiddleName(), boldFont));
        document.close();
    }
}
