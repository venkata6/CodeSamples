//
//  TestViewController.m
//  Test
//
//  Created by Venkatesh Arjunan on 9/23/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "TestViewController.h"
#import "Person.h"
#import "Events.h"

@interface TestViewController ()
@property (nonatomic,retain) UIBarButtonItem *addButton;
@property (nonatomic,retain) UIManagedDocument *document;
//@property (nonatomic,retain) ;
@end


@implementation TestViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.

    // Set the title.
    self.title = @"Test";
        
    self.addButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(addEvent)];
    self.addButton.enabled = YES;
    self.navigationItem.rightBarButtonItem = self.addButton;
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)addEvent {
    
   [self performSegueWithIdentifier: @"InputData" sender: self];

}

- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ( [[segue identifier] isEqualToString:@"InputData"] )
    {
        InputViewController* destController = [segue destinationViewController];
        [destController setDelegate:self];
        [destController setTitle:@"Input details"];
    }
}


// Implement the protocal delegate for itemInputController to get the first name, last name etc

- (void) didAddContact:(ContactData *)contactData {
    if (contactData) {
        // Create and configure a new instance of the Event entity.
        /* new database code start  */
        NSURL *documentDir = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
        
        NSURL *storeURL = [documentDir URLByAppendingPathComponent:@"Test"];
        
        self.document = [[UIManagedDocument alloc] initWithFileURL:(NSURL *)storeURL];
        NSDictionary *options = [NSDictionary dictionaryWithObjectsAndKeys:
                                 [NSNumber numberWithBool:YES], NSMigratePersistentStoresAutomaticallyOption,
                                 [NSNumber numberWithBool:YES], NSInferMappingModelAutomaticallyOption, nil];
        self.document.persistentStoreOptions = options;
        
        if ([[NSFileManager defaultManager] fileExistsAtPath:[storeURL path]]) {
            [self.document openWithCompletionHandler:^(BOOL success){
                if (success)  [self documentIsReady:contactData];
                if (!success) NSLog( @"couldnt open document");
            }];
        }
        else {
            // file does not exist , hence create it
            [self.document saveToURL:storeURL forSaveOperation:UIDocumentSaveForCreating completionHandler:^(BOOL success){
                if (success)  [self documentIsReady:contactData];
                if (!success) NSLog( @"couldnt create document");
            }];
        }
    }
}

- (void)documentIsReady: (ContactData *)contactData {
    
    if (self.document.documentState == UIDocumentStateNormal) {
       NSManagedObjectContext *context = self.document.managedObjectContext;
            

       Events *event = [NSEntityDescription insertNewObjectForEntityForName:@"Events"
                                                      inManagedObjectContext:context];
       event.points = [NSNumber numberWithInt:[contactData.points intValue]];
       event.notes  = contactData.notes;
       event.date = [NSDate date] ;
        event.id = [NSNumber numberWithInt:1];
        
        
       Person *person = [NSEntityDescription insertNewObjectForEntityForName:@"Person"
                                                        inManagedObjectContext:context];
       person.firstName = contactData.firstName;
       person.lastName = contactData.lastName;
       person.emailAddr = contactData.emailAddr;
       [person addHasDeedsObject:event];
       event.doneBy = person ;
       NSError * error = nil;
       if (![context save:&error]) {
         if (error) NSLog( @"couldnt save the Events entity");
       }
    
    }
}

@end
