#import <Cocoa/Cocoa.h>

@interface NSTabView : NSView {
    IBOutlet id delegate;
}
- (IBAction)buttonPressed:(id)sender;
- (IBAction)selectFirstTabViewItem:(id)sender;
- (IBAction)selectLastTabViewItem:(id)sender;
- (IBAction)selectNextTabViewItem:(id)sender;
- (IBAction)selectPreviousTabViewItem:(id)sender;
- (IBAction)takeSelectedTabViewItemFromSender:(id)sender;
@end
