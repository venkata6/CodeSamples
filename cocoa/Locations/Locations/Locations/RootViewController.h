//
//  RootViewController.h
//  Locations
//
//  Created by Venkatesh Arjunan on 2/16/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "ItemInputViewController.h"
#import <AddressBook/AddressBook.h>
#import <AddressBookUI/AddressBookUI.h>
#import "Person.h"

@class DetailViewController;

@interface RootViewController : UITableViewController<CLLocationManagerDelegate,ContactAddDelegate,NSFetchedResultsControllerDelegate>{
    
    NSMutableArray *eventsArray;
    NSManagedObjectContext *managedObjectContext;
    
    CLLocationManager *locationManager;
    UIBarButtonItem *addButton;
    UIManagedDocument *document;
    //ItemInputViewController *itemInputViewController;
    //ABNewPersonViewController *abNewPersonViewController;
    
}

@property (nonatomic,retain) NSMutableArray *eventsArray;
@property (nonatomic,retain) NSManagedObjectContext *managedObjectContext;
@property (nonatomic,retain) UIManagedDocument *document;

@property (nonatomic,retain) CLLocationManager *locationManager;
@property (nonatomic,retain) UIBarButtonItem *addButton;

@property (strong, nonatomic) DetailViewController *detailViewController;
@property (strong, nonatomic) NSFetchedResultsController *fetchedResultsController;

- (void)addEvent;

@end
