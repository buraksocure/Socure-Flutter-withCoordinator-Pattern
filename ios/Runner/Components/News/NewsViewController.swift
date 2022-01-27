
import UIKit
import SocureSdk

class NewsViewController: UIViewController {
    var coordinatorDelegate: NewsCoordinatorDelegate?
    
    var referenceViewController:ViewController?
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


extension NewsViewController: ImageCallback  {
    func documentFrontCallBack(docScanResult: DocScanResult) {
        guard let imageData = docScanResult.imageData,
            let image = UIImage.init(data: imageData) else {
                return
        }
        
        referenceViewController?.frontDocumentData = imageData
        referenceViewController?.frontImageView.image = image
        
        if( referenceViewController?.backDocumentData != nil &&
                   referenceViewController?.frontDocumentData != nil) {
            self.dismiss(animated: true, completion: nil)
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
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    func selfieCallBack(selfieScanResult: SelfieScanResult) {
    }
    
    func onScanCancelled() {
        self.dismiss(animated: true, completion: nil)
    }
    
    func onError(errorType: SocureSDKErrorType, errorMessage: String) {
        print(errorType)
        print(errorMessage)
        
        
            
        
        if errorType == .DocumentScanFailedError || errorType == .DocumentScanFailedError {
            //self.dismiss(animated: true, completion: nil)
        }
    }
    

}

extension NewsViewController: BarcodeCallback {
    func handleBarcodeData(barcodeData: BarcodeData?) {
        guard let barcodeData = barcodeData else {
            print("Barcode data not found")
            return
        }
            
        print("Barcode data is \(barcodeData)")
        
    }
}
