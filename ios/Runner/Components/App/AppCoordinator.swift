import Foundation
import UIKit

class AppCoordinator: BaseCoordinator{
    weak var navigationController: UINavigationController?
    
    init(navigationController: UINavigationController) {
        super.init()
        self.navigationController = navigationController
    }
    
    override func start() {
        super.start()
        navigateToSocureViewController()
    }
    
}

protocol SocureToAppCoordinatorDelegate: class {
    func navigateToFlutterViewController()
}

protocol FlutterToAppCoordinatorDelegate: class {
    func navigateToSocureViewController()
}

extension AppCoordinator: SocureToAppCoordinatorDelegate{
    func navigateToFlutterViewController(){
        let coordinator = FlutterCoordinator(navigationController: self.navigationController)
        coordinator.delegate = self
        self.add(coordinator)
        coordinator.start()
    }
}

extension AppCoordinator: FlutterToAppCoordinatorDelegate{
    func navigateToSocureViewController(){
        let coordinator = SocureCoordinator(navigationController: self.navigationController)
        coordinator.delegate = self
        self.add(coordinator)
        coordinator.start()
    }
}
