//
//  DetailViewController.h
//  Locations
//
//  Created by Venkatesh Arjunan on 2/16/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MessageUI/MessageUI.h>


@interface DetailViewController : UIViewController <UISplitViewControllerDelegate,MFMailComposeViewControllerDelegate>

@property (strong, nonatomic) id detailItem;

- (IBAction)sendMail:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *detailDescriptionLabel;
@end
