package coworking.rentalsystem.controller;

import com.lowagie.text.DocumentException;
import coworking.rentalsystem.model.StatusType;
import coworking.rentalsystem.model.entity.*;
import coworking.rentalsystem.model.form.*;
import coworking.rentalsystem.model.form.*;
import coworking.rentalsystem.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RentSystemController {

    private ServiceFactory serviceFactory = new ServiceFactory();

    @RequestMapping(value = "/tariff", method = {RequestMethod.GET})
    public String getTariffs(Model model) {
        IWorkplaceService workplaceService = serviceFactory.getWorkplaceService();
        ArrayList<Tariff> tariffs = workplaceService.getTariffs();
        model.addAttribute("tariffs", tariffs);
        return "tariffsPage";
    }

    @RequestMapping(value = "/tariff/cheap", method = {RequestMethod.GET})
    public String getCheapTariffs(Model model) {
        IWorkplaceService workplaceService = serviceFactory.getWorkplaceService();
        ArrayList<Tariff> tariffs = workplaceService.getCheapTariffs();
        model.addAttribute("tariffs", tariffs);
        return "tariffsPage";
    }
    @RequestMapping(value = "/tariff/expensive", method = {RequestMethod.GET})
    public String getExpensiveTariffs(Model model) {
        IWorkplaceService workplaceService = serviceFactory.getWorkplaceService();
        ArrayList<Tariff> tariffs = workplaceService.getExpensiveTariffs();
        model.addAttribute("tariffs", tariffs);
        return "tariffsPage";
    }

    @RequestMapping(value = "/equipment", method = {RequestMethod.GET})
    public String getEquipmentTypes(Model model) {
        IEquipmentService equipmentService = serviceFactory.getEquipmentService();
        ArrayList<EquipmentType> equipmentTypes = equipmentService.getEquipmentType();
        model.addAttribute("equipmentTypes", equipmentTypes);
        return "generalEquipmentPage";
    }

    @RequestMapping(value = "/tariff/{id}/rooms", method = {RequestMethod.GET})
    public String getRooms(ModelMap model, @PathVariable(value = "id") int id) {
        IWorkplaceService workplaceService = serviceFactory.getWorkplaceService();
        ArrayList<Room> rooms = workplaceService.getRoomByTariff(id);
        model.addAttribute("rooms", rooms);
        String duration = workplaceService.getDurationByTariff(id);
        model.addAttribute("duration", duration);
        model.addAttribute("isFill", false);
        model.addAttribute("rentForm", new RentForm());
        return "roomPage";
    }

    @RequestMapping(value = "/tariff/{id}/rooms", method = {RequestMethod.POST})
    public String getAvailableRooms(ModelMap model, @ModelAttribute("rentForm") RentForm rentForm,
                                    @ModelAttribute("duration") String duration, @PathVariable(value = "id") int id) {
        IWorkplaceService workplaceService = serviceFactory.getWorkplaceService();
        ArrayList<Room> rooms = workplaceService.getAvailableRoomByTariff(Timestamp.valueOf(rentForm.getDateFrom()),
                Timestamp.valueOf(rentForm.getDateTo()), id);
        model.addAttribute("rooms", rooms);
        model.addAttribute("isFill", true);
        rentForm.setId_tariff(id);
        model.addAttribute("filledRentForm", rentForm);
        return "roomPage";
    }
    @RequestMapping(value = "/success", method = {RequestMethod.POST})
    public String createRentApplication(Model model, @ModelAttribute("filledRentForm") RentForm rentForm){
        RentApplication rentApplication = new RentApplication();
        rentApplication.setRentStart(Timestamp.valueOf(rentForm.getDateFrom()));
        rentApplication.setRentEnd(Timestamp.valueOf(rentForm.getDateTo()));

        Tariff tariff = new Tariff();
        rentApplication.setTariff(tariff);
        rentApplication.getTariff().setId(rentForm.getId_tariff());

        IWorkplaceService workplaceService = serviceFactory.getWorkplaceService();
        rentApplication.setPlace(workplaceService.getAvailablePlaceByRoom(rentApplication.getRentStart(),
                rentApplication.getRentEnd(), rentForm.getId_room()));

        IUserService userService = serviceFactory.getUserService();
        rentApplication.setUser(userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));

        IRentService rentService = serviceFactory.getRentService();
        model.addAttribute("id_rent_application",rentService.insertRentApplication(rentApplication));
        return "successPage";
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public String loginUser(Model model) {
        User userForm = new User();
        model.addAttribute("userForm", userForm);
        return "loginPage";
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public String loginUser() {
        return "redirect:/tariff";
    }

    @RequestMapping(value = "/registration", method = {RequestMethod.GET})
    public String registerUser(Model model) {
        User userForm = new User();
        model.addAttribute("userForm", userForm);
        return "registrationPage";
    }

    @RequestMapping(value = "/registration", method = {RequestMethod.POST})
    public String registerUser(@ModelAttribute User user) {
        IUserService userService = serviceFactory.getUserService();
        userService.addUser(user);
        return "redirect:/login";
    }

    @RequestMapping(value = "/user/profile", method = {RequestMethod.GET})
    public String getUserProfile(Model model) {
        IUserService userService = serviceFactory.getUserService();
        User user = userService.getPersonalDataByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("userInf", user);
        return "userProfilePage";
    }

    @RequestMapping(value = "/user/myApplication", method = {RequestMethod.GET})
    public String getUserApplication(ModelMap model) {
        IRentService rentService = serviceFactory.getRentService();
        ArrayList<RentApplication> rentApplications = rentService.getUserApplication(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("rentApplications", rentApplications);

        ArrayList<Status> statuses = rentService.getAllStatus();
        model.addAttribute("statuses", statuses);
        StatusForm statusForm = new StatusForm();
        model.addAttribute("statusForm",statusForm);
        return "userApplicationPage";
    }

    @RequestMapping(value = "/user/myApplication", method = {RequestMethod.POST})
    public String getUserApplication(ModelMap model, @ModelAttribute StatusForm filledForm) {
        IRentService rentService = serviceFactory.getRentService();
        ArrayList<RentApplication> rentApplications;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(filledForm.getChosenStatus() == 0)
            rentApplications = rentService.getUserApplication(username);
        else
            rentApplications = rentService.getUserApplicationByStatus(username, filledForm.getChosenStatus());

        model.addAttribute("rentApplications", rentApplications);

        ArrayList<Status> statuses = rentService.getAllStatus();
        model.addAttribute("statuses", statuses);
        model.addAttribute("statusForm",filledForm);
        return "userApplicationPage";
    }

    @RequestMapping(value = "/updateStatus", method = {RequestMethod.POST})
    public String updateUserApplication( @RequestParam int idApplication, @RequestParam int newStatus) {
        IRentService rentService = serviceFactory.getRentService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
            rentService.updateStatusApplication(idApplication, newStatus);
            return "redirect:/user/myApplication";
        }
        else{
            IUserService userService = serviceFactory.getUserService();
            rentService.updateStatusApplication(userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId(), idApplication, newStatus);
            return "redirect:/admin/new";
        }
    }

    @RequestMapping(value = "/admin/new", method = {RequestMethod.GET})
    public String getNewApplication(Model model) {
        IRentService rentService = serviceFactory.getRentService();
        ArrayList<RentApplication> rentApplications = rentService.getAllApplicationByStatus(StatusType.NEW.getId());
        model.addAttribute("rentApplications", rentApplications);
        model.addAttribute("status", StatusType.NEW);
        return "adminCabinetPage";
    }

    @RequestMapping(value = "/admin/confirmed", method = {RequestMethod.GET})
    public String getConfirmedApplication(Model model) {
        IRentService rentService = serviceFactory.getRentService();
        ArrayList<RentApplication> rentApplications = rentService.getAllApplicationByStatus(StatusType.CONFIRMED.getId());
        model.addAttribute("rentApplications", rentApplications);
        model.addAttribute("status", StatusType.CONFIRMED);
        return "adminCabinetPage";
    }
    @RequestMapping(value = "/admin/canceled", method = {RequestMethod.GET})
    public String getCanceledApplication(Model model) {
        IRentService rentService = serviceFactory.getRentService();
        ArrayList<RentApplication> rentApplications = rentService.getAllApplicationByStatus(StatusType.CANCELED.getId());
        model.addAttribute("rentApplications", rentApplications);
        model.addAttribute("status", StatusType.CANCELED);
        return "adminCabinetPage";
    }
    @RequestMapping(value = "/admin/rejected", method = {RequestMethod.GET})
    public String getRejectedApplication(Model model) {
        IRentService rentService = serviceFactory.getRentService();
        ArrayList<RentApplication> rentApplications = rentService.getAllApplicationByStatus(StatusType.REJECTED.getId());
        model.addAttribute("rentApplications", rentApplications);
        model.addAttribute("status", StatusType.REJECTED);
        return "adminCabinetPage";
    }
    @RequestMapping(value = "/admin/paid", method = {RequestMethod.GET})
    public String getPaidApplication(Model model) {
        IRentService rentService = serviceFactory.getRentService();
        ArrayList<RentApplication> rentApplications = rentService.getAllApplicationByStatus(StatusType.PAID.getId());
        model.addAttribute("rentApplications", rentApplications);
        model.addAttribute("status", StatusType.PAID);
        return "adminCabinetPage";
    }

    @RequestMapping(value = "/viewLeaseAgreement", method = {RequestMethod.POST})
    public void viewDocument(@RequestParam int idApplication, HttpServletResponse response) throws IOException, DocumentException {
        IRentService rentService = serviceFactory.getRentService();
        RentApplication rentApplication = rentService.getRentApplicationById(idApplication);
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=lease_agreement_" + rentApplication.getLeaseAgreement()+ ".pdf";
        response.setHeader(headerKey, headerValue);

        DocumentService exporter = new DocumentService();
        exporter.export(rentApplication, response);
    }

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public String error(){
        return "errorPage";
    }
}
