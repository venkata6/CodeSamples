#import "TwitterViewController.h"

@implementation TwitterViewController
- (IBAction)add:(id)sender {
    
}

- (IBAction)buttonPressed:(id)sender {
 NSLog(@ "button pressed");
 id str =  @"button pressed";
 NSMutableData *receivedData;
 //[displayText setStringValue:str];
 
 // Create the request.
	NSURLRequest *theRequest=[NSURLRequest requestWithURL:[NSURL URLWithString:@"http://www.cricket.yahoo.com/"]
                        cachePolicy:NSURLRequestUseProtocolCachePolicy
                    timeoutInterval:60.0];
	// create the connection with the request
	// and start loading the data
	NSURLConnection *theConnection=[[NSURLConnection alloc] initWithRequest:theRequest delegate:self];
	if (theConnection) {
    // Create the NSMutableData to hold the received data.
    // receivedData is an instance variable declared elsewhere.
		receivedData = [[NSMutableData data] retain];
		NSLog( [receivedData bytes] ) ;
		//id str = [receivedData bytes];
		[displayText setStringValue:str];
	} else {
    // Inform the user that the connection failed.
	}
	
	      
}

- (IBAction)fetch:(id)sender {
    
}

- (IBAction)remove:(id)sender {
    
}
@end
