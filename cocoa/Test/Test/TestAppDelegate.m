//
//  TestAppDelegate.m
//  Test
//
//  Created by Venkatesh Arjunan on 9/23/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "TestAppDelegate.h"
#import "NSString+MD5.h"

@interface TestAppDelegate ()
@end

@implementation TestAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
  NSURL *documentDir = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
    
    NSURL *storeURL = [documentDir URLByAppendingPathComponent:@"Test"];
    if ( self.document == nil) {
        self.document = [[UIManagedDocument alloc] initWithFileURL:(NSURL *)storeURL];
        NSDictionary *options = [NSDictionary dictionaryWithObjectsAndKeys:
                                 [NSNumber numberWithBool:YES], NSMigratePersistentStoresAutomaticallyOption,
                                 [NSNumber numberWithBool:YES], NSInferMappingModelAutomaticallyOption, nil];
        self.document.persistentStoreOptions = options;
    }
    
   /* if ([[NSFileManager defaultManager] fileExistsAtPath:[storeURL path]]) {
        [self.document openWithCompletionHandler:^(BOOL success){
            if (success) {
                //[self fetchEntitiesFromDatabase];
                NSLog( @"Successfully opened document");
            };
            if (!success) NSLog( @"couldnt open document");
        }];
    }*/
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

// let us put all database operations here

- (void)isDocumentReadyToSave: (ContactData *)contactData {
    
    if (self.document.documentState == UIDocumentStateNormal) {
        NSManagedObjectContext *context = self.document.managedObjectContext;
        
        
        Events *event = [NSEntityDescription insertNewObjectForEntityForName:@"Events"
                                                      inManagedObjectContext:context];
        event.points = [NSNumber numberWithInt:[contactData.points intValue]];
        event.notes  = contactData.notes;
        event.date = [NSDate date] ;
        event.id = [NSNumber numberWithInt:1];
        event.recurrence = contactData.reminderFreq ;
        NSString* md5 = nil ;
        if ( contactData.selectedPerson != nil ) {
            md5 = [[contactData.selectedPerson.firstName stringByAppendingString:contactData.selectedPerson.lastName ] MD5];
        }
        else {
            md5 = [[contactData.firstName stringByAppendingString:contactData.lastName ] MD5];
        }
        Person *alreadyExists = [self checkIfPersonExists:md5];
        
        if ( alreadyExists == nil ) {
            
            Person *person = [NSEntityDescription insertNewObjectForEntityForName:@"Person"
                                                           inManagedObjectContext:context];
            if (contactData.selectedPerson != nil) {
                person.firstName = contactData.selectedPerson.firstName;
                person.lastName = contactData.selectedPerson.lastName;
                person.emailAddr = contactData.selectedPerson.emailAddr;
                person.phoneNumber = contactData.selectedPerson.phoneNumber;
            }
            else {
                person.firstName = contactData.firstName;
                person.lastName = contactData.lastName;
                person.emailAddr = contactData.emailAddr;
                person.phoneNumber = contactData.phoneNo;
            }
            person.id = md5 ;
            [person addHasDeedsObject:event];
            event.doneBy = person ;
        }
        else {
            event.doneBy = alreadyExists ;
        }
        NSError * error = nil;
        if (![context save:&error]) {
            if (error) NSLog( @"couldnt save the Events entity");
        }
        [self fetchEntitiesFromDatabase ] ;
        
    }
}

-(void) saveDocument: (ContactData *)contactData {
    // Create and configure a new instance of the Event entity.
 
    NSURL *documentDir = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
 
    NSURL *storeURL = [documentDir URLByAppendingPathComponent:@"Test"];
    if ( !self.document ) {
        self.document = [[UIManagedDocument alloc] initWithFileURL:(NSURL *)storeURL];
        NSDictionary *options = [NSDictionary dictionaryWithObjectsAndKeys:
                                 [NSNumber numberWithBool:YES], NSMigratePersistentStoresAutomaticallyOption,
                                 [NSNumber numberWithBool:YES], NSInferMappingModelAutomaticallyOption, nil];
        self.document.persistentStoreOptions = options;
    }
 
    if ([[NSFileManager defaultManager] fileExistsAtPath:[storeURL path]]) {
        [self.document openWithCompletionHandler:^(BOOL success){
            if (success)  [self isDocumentReadyToSave:contactData];
            if (!success) NSLog( @"couldnt open document");
        }];
    }
    else {
        // file does not exist , hence create it
        [self.document saveToURL:storeURL forSaveOperation:UIDocumentSaveForCreating completionHandler:^(BOOL success){
            if (success)  [self isDocumentReadyToSave:contactData];
            if (!success) NSLog( @"couldnt create document");
        }];
    }
}



- (Person*) checkIfPersonExists: (NSString*) md5 {
    
    if (self.document.documentState == UIDocumentStateNormal) {
        NSManagedObjectContext *context = self.document.managedObjectContext;
        NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Person"];
        
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"id == %@", md5];
        
        [request setPredicate:predicate];
        
        NSError *error;
        NSArray *personArr = [context executeFetchRequest:request error:&error];
        
        if ( !self.personArray || error) {
            NSLog(@"error in fetching Person entity, no object returned");
            
        }
        if ( personArr == nil || ([personArr count] == 0)  ) {
            return nil;
        }
        return [personArr objectAtIndex:0] ;
    }
    return nil;
}


- (void)fetchEntitiesFromDatabase {
    
    if (self.document.documentState == UIDocumentStateNormal) {
        NSManagedObjectContext *context = self.document.managedObjectContext;
        NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Person"];
        
        [request setRelationshipKeyPathsForPrefetching:
         [NSArray arrayWithObject:@"hasDeeds"]];
        
        NSSortDescriptor *sortByName = [NSSortDescriptor sortDescriptorWithKey:@"firstName"  ascending:YES];
        request.sortDescriptors = @[sortByName];
        
        NSError *error;
        self.personArray = [context executeFetchRequest:request error:&error];
        
        if ( !self.personArray || error) {
            NSLog(@"error in fetching Person entity, no object returned");
            
        }
    }
}

- (NSArray*) getReminders: (NSString*) recurrence   {
    
    if (self.document.documentState == UIDocumentStateNormal) {
        NSManagedObjectContext *context = self.document.managedObjectContext;
        NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Events"];
       
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"recurrence == %@", recurrence];
        [request setPredicate:predicate];

        NSError *error;
        NSArray* eventsArray = [context executeFetchRequest:request error:&error];
        
        if ( !eventsArray || error) {
            NSLog(@"error in fetching Person entity, no object returned");
            
        }
        else {
            for (Events* e  in eventsArray) {
                NSLog(e.doneBy.firstName);
            }
        }
        return eventsArray;
    }
    return nil;
}




@end
