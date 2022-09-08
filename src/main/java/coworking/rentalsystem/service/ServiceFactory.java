package coworking.rentalsystem.service;


public class ServiceFactory {
    public ServiceFactory(){  }

    public IWorkplaceService getWorkplaceService(){
        return new WorkplaceService();
    }

    public IEquipmentService getEquipmentService(){ return new EquipmentService(); }

    public IUserService getUserService(){ return new UserService(); }

    public IRentService getRentService(){return new RentService(); }
}
