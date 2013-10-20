//
//  DetailViewController.m
//  Locations
//
//  Created by Venkatesh Arjunan on 2/16/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "DetailViewController.h"

@interface DetailViewController ()
@property (strong, nonatomic) UIPopoverController *masterPopoverController;
- (void)configureView;
@end

@implementation DetailViewController

#pragma mark - Managing the detail item

- (void)setDetailItem:(id)newDetailItem
{
    if (_detailItem != newDetailItem) {
        _detailItem = newDetailItem;
        
        // Update the view.
        [self configureView];
    }

    if (self.masterPopoverController != nil) {
        [self.masterPopoverController dismissPopoverAnimated:YES];
    }        
}

- (void)configureView
{
    // Update the user interface for the detail item.

    if (self.detailItem) {
        if ( self.detailDescriptionLabel )
        {
            self.detailDescriptionLabel.text = [[self.detailItem valueForKey:@"notes"] description];
            [self.detailDescriptionLabel setNumberOfLines:0];
            [self.detailDescriptionLabel sizeToFit];
        //    self.eventDate.text =  [[self.detailItem valueForKey:@"eventDate"] description];
        }
    }
}

- (void)setUILabelTextWithVerticalAlignTop:(NSString *)theText {
    // labelSize is hard-wired but could use constants to populate the size
    CGSize labelSize = CGSizeMake(250, 50);
    CGSize theStringSize = [theText sizeWithFont:self.detailDescriptionLabel.font constrainedToSize:labelSize lineBreakMode:self.detailDescriptionLabel.lineBreakMode];
    self.detailDescriptionLabel.frame = CGRectMake(self.detailDescriptionLabel.frame.origin.x, self.detailDescriptionLabel.frame.origin.y, theStringSize.width, theStringSize.height);
    self.detailDescriptionLabel.text = theText;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [self configureView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Split view

- (void)splitViewController:(UISplitViewController *)splitController willHideViewController:(UIViewController *)viewController withBarButtonItem:(UIBarButtonItem *)barButtonItem forPopoverController:(UIPopoverController *)popoverController
{
    barButtonItem.title = NSLocalizedString(@"Master", @"Master");
    [self.navigationItem setLeftBarButtonItem:barButtonItem animated:YES];
    self.masterPopoverController = popoverController;
}

- (void)splitViewController:(UISplitViewController *)splitController willShowViewController:(UIViewController *)viewController invalidatingBarButtonItem:(UIBarButtonItem *)barButtonItem
{
    // Called when the view is shown again in the split view, invalidating the button and popover controller.
    [self.navigationItem setLeftBarButtonItem:nil animated:YES];
    self.masterPopoverController = nil;
}

- (IBAction)sendMail:(id)sender {
  
    MFMailComposeViewController *picker = [[MFMailComposeViewController alloc] init];
    picker.mailComposeDelegate = self;  // &lt;- very important step if you want feedbacks on what the user did with your email sheet
    
    [picker setSubject:@"Thank You!"];
    NSArray * toRecipients = [NSArray arrayWithObject: [[self.detailItem valueForKey:@"emailAddr"] description]];
    
    [picker setToRecipients: toRecipients];
    
    // Fill out the email body text
    NSString *emailBody =
    [NSString stringWithFormat:@"%@\n\n", self.detailDescriptionLabel.text];
    
    [picker setMessageBody:emailBody isHTML:YES]; // depends. Mostly YES, unless you want to send it as plain text (boring)
    
    picker.navigationBar.barStyle = UIBarStyleBlack; // choose your style, unfortunately, Translucent colors behave quirky.
    
    [self presentViewController:picker animated:YES completion:nil];

    
 }

// Dismisses the email composition interface when users tap Cancel or Send. Proceeds to update the message field with the result of the operation.

- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
    UIAlertView *alert ;
    // Notifies users about errors associated with the interface
    switch (result)
    {
        case MFMailComposeResultCancelled:
            break;
        case MFMailComposeResultSaved:
            break;
        case MFMailComposeResultSent:
            alert = [[UIAlertView alloc] initWithTitle:@"" message:@"email sent successfully!"
                                                           delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
            break;
        case MFMailComposeResultFailed:
            break;
            
        default:
        {
            alert = [[UIAlertView alloc] initWithTitle:@"" message:@"Sending Failed - Unknown Error :-("
                                                           delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
        }
           break;
    }
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
