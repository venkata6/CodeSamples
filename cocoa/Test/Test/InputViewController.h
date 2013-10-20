//
//  InputViewController.h
//  Test
//
//  Created by Venkatesh Arjunan on 9/26/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AddressBookUI/AddressBookUI.h>



@interface InputViewController : UIViewController <ABPeoplePickerNavigationControllerDelegate>

//@property (nonatomic, assign) id<ContactAddDelegate> delegate;
@property (strong, nonatomic) NSArray* existingPersons;

@end

