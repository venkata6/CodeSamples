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

@end
