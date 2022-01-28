import Foundation
import UIKit

final class SocureCoordinator: BaseCoordinator{
    weak var navigationController: UINavigationController?
    weak var delegate: SocureToAppCoordinatorDelegate?
    
    override func start() {
        super.start()
        let storyboard = UIStoryboard(name: "Socure", bundle: nil)
        if let container = storyboard.instantiateViewController(withIdentifier: "SocureViewController") as? SocureViewController {
            container.coordinatorDelegate = self
            navigationController?.pushViewController(container, animated: false)
        }
    }
    
    init(navigationController: UINavigationController?) {
        super.init()
        self.navigationController = navigationController
    }
}

protocol SocureCoordinatorDelegate {
    func navigateToFlutter()
}

extension SocureCoordinator: SocureCoordinatorDelegate{
    func navigateToFlutter(){
        self.delegate?.navigateToFlutterViewController()
    }
}
