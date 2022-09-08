package coworking.rentalsystem.service;

import com.lowagie.text.DocumentException;
import coworking.rentalsystem.model.entity.RentApplication;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IDocumentService {
    void export(RentApplication rentApplication, HttpServletResponse response) throws DocumentException, IOException;
}
