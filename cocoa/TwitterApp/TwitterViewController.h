#import <Cocoa/Cocoa.h>

@interface TwitterViewController : NSObjectController {
    IBOutlet id content;
    IBOutlet NSManagedObjectContext *managedObjectContext;
    IBOutlet id displayText;
}
- (IBAction)add:(id)sender;
- (IBAction)buttonPressed:(id)sender;
- (IBAction)fetch:(id)sender;
- (IBAction)remove:(id)sender;
@end
