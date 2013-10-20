//
//  TestAppDelegate.h
//  Test
//
//  Created by Venkatesh Arjunan on 9/23/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Person.h"
#import "Events.h"
#import "InputEventViewController.h"

@interface TestAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic,retain) UIManagedDocument *document;
@property (nonatomic,retain) NSArray *personArray;

- (void)fetchEntitiesFromDatabase ;
- (void)saveDocument: (ContactData *)contactData ;
- (NSArray*) getReminders: (NSString*) recurrence ;
@end

// value object definition starts here 

