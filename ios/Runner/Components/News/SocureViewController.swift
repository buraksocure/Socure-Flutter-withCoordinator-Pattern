
import UIKit
import SocureSdk

class SocureViewController: UIViewController {
    var coordinatorDelegate: SocureCoordinatorDelegate?
    
    var referenceViewController:SocureController?
    let d​ocScanner​ = DocumentScanner()

    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor.systemBlue
        // Do any additional setup after loading the view.
        self.d​ocScanner​.initiateLicenseScan(ImageCallback: self, BarcodeCallback: self)
        
      
        
    }

 
    
    @IBAction func goToFlutter(_ sender: Any) {
        coordinatorDelegate?.navigateToFlutter()
    }

}


extension SocureViewController: ImageCallback  {
    func documentFrontCallBack(docScanResult: DocScanResult) {
        guard let imageData = docScanResult.imageData,
            let image = UIImage.init(data: imageData) else {
                return
        }
        
        referenceViewController?.frontDocumentData = imageData
        referenceViewController?.frontImageView.image = image
        
        if( referenceViewController?.backDocumentData != nil &&
                   referenceViewController?.frontDocumentData != nil) {
            coordinatorDelegate?.navigateToFlutter()

        }
    }
    
    func documentBackCallBack(docScanResult: DocScanResult) {
        print(documentBackCallBack)
        guard let imageData = docScanResult.imageData,
            let image = UIImage.init(data: imageData) else {
                return
        }
        referenceViewController?.backDocumentData = imageData
        referenceViewController?.backImageView.image = image
        
        // MARK: Dismiss here
        if( referenceViewController?.backDocumentData != nil &&
            referenceViewController?.frontDocumentData != nil) {
            coordinatorDelegate?.navigateToFlutter()

        }
    }
    
    func selfieCallBack(selfieScanResult: SelfieScanResult) {
    }
    
    func onScanCancelled() {
        coordinatorDelegate?.navigateToFlutter()

    }
    
    func onError(errorType: SocureSDKErrorType, errorMessage: String) {
        print(errorType)
        print(errorMessage)
        
        
            
        
        if errorType == .DocumentScanFailedError || errorType == .DocumentScanFailedError {
            coordinatorDelegate?.navigateToFlutter()

        }
    }
    

}

extension SocureViewController: BarcodeCallback {
    func handleBarcodeData(barcodeData: BarcodeData?) {
        guard let barcodeData = barcodeData else {
            print("Barcode data not found")
            return
        }
            
        print("Barcode data is \(barcodeData)")
        
    }
}
