//
//  TestTableViewController.m
//  Test
//
//  Created by Venkatesh Arjunan on 9/29/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "TestTableViewController.h"
#import "Person.h"
#import "Events.h"
#import "NSString+MD5.h"


@interface TestTableViewController ()
@property (nonatomic,retain) UIBarButtonItem *addButton;
@property (nonatomic,retain) UIManagedDocument *document;
@property (nonatomic,retain) NSArray *personArray;
@end

@implementation TestTableViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    // Set the title.
    self.title = @"Latest";
    
    self.addButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(addEvent)];
    self.addButton.enabled = YES;
    self.navigationItem.rightBarButtonItem = self.addButton;

    NSURL *documentDir = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];

    NSURL *storeURL = [documentDir URLByAppendingPathComponent:@"Test"];
    if ( self.document == nil) {
        self.document = [[UIManagedDocument alloc] initWithFileURL:(NSURL *)storeURL];
        NSDictionary *options = [NSDictionary dictionaryWithObjectsAndKeys:
                                 [NSNumber numberWithBool:YES], NSMigratePersistentStoresAutomaticallyOption,
                                 [NSNumber numberWithBool:YES], NSInferMappingModelAutomaticallyOption, nil];
        self.document.persistentStoreOptions = options;
    }
    
    if ([[NSFileManager defaultManager] fileExistsAtPath:[storeURL path]]) {
        [self.document openWithCompletionHandler:^(BOOL success){
            if (success)  [self fetchEntitiesFromDatabase];
            if (!success) NSLog( @"couldnt open document");
        }];
    }

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
        [destController setTitle:@"Add person "];
        [destController setExistingPersons:self.personArray];
    }
}

// Implement the protocal delegate for itemInputController to get the first name, last name etc

- (void) didAddContact:(ContactData *)contactData {
    if (contactData) {
        // Create and configure a new instance of the Event entity.
        /* new database code start  */
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
        

        NSString* md5 = [[contactData.firstName stringByAppendingString:contactData.lastName] MD5];
        Person *alreadyExists = [self checkIfPersonExists:md5];

        if ( alreadyExists == nil ) {

            Person *person = [NSEntityDescription insertNewObjectForEntityForName:@"Person"
                                                       inManagedObjectContext:context];
            person.firstName = contactData.firstName;
            person.lastName = contactData.lastName;
            person.emailAddr = contactData.emailAddr;
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
        [[self tableView] reloadData];
    }
}



#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    if ( self.personArray ) {
        return self.personArray.count ;
    }
    else {
        return 0 ;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Person";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    
    Person * person = (Person *)[self.personArray objectAtIndex:indexPath.row];
    NSArray *deeds = [[[self.personArray objectAtIndex:indexPath.row] hasDeeds] allObjects];
    long points = 0 ;
    for ( Events *event in deeds) {
        points += [event.points longValue];
        
    }
    
    NSString *string = [NSString stringWithFormat:@"%@ - %@ %ld Points",
                        person.firstName,
                        person.lastName, points ];
                        
    cell.textLabel.text = string;
    return cell;
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}

@end
