//
//  TestTableViewController.m
//  Test
//
//  Created by Venkatesh Arjunan on 9/29/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "TestTableViewController.h"
#import "TestAppDelegate.h"
#import "Person.h"
#import "Events.h"


@interface TestTableViewController ()
@property (nonatomic,retain) UIBarButtonItem *addButton;
@property (nonatomic,retain) TestAppDelegate *appDelegate;
@property (nonatomic,retain) NSFetchedResultsController* fetchedResultsController;
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
    
    self.appDelegate = (TestAppDelegate *) [[UIApplication sharedApplication] delegate];

    
    // Set the title.
    self.title = @"Latest";
    
    self.addButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(addEvent)];
    self.addButton.enabled = YES;
    self.navigationItem.rightBarButtonItem = self.addButton;
   
    // NSFetchedResultsController initialization
    
    // Override point for customization after application launch.
    NSURL *documentDir = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
    
    NSURL *storeURL = [documentDir URLByAppendingPathComponent:@"Test"];
    if ( self.appDelegate.document == nil) {
        self.appDelegate.document = [[UIManagedDocument alloc] initWithFileURL:(NSURL *)storeURL];
        NSDictionary *options = [NSDictionary dictionaryWithObjectsAndKeys:
                                 [NSNumber numberWithBool:YES], NSMigratePersistentStoresAutomaticallyOption,
                                 [NSNumber numberWithBool:YES], NSInferMappingModelAutomaticallyOption, nil];
        self.appDelegate.document.persistentStoreOptions = options;
    }

    if ([[NSFileManager defaultManager] fileExistsAtPath:[storeURL path]]) {
        [self.appDelegate.document openWithCompletionHandler:^(BOOL success){
            if (success) {
                //[self fetchEntitiesFromDatabase];
                 NSLog( @"Successfully opened document");
                [self doFetchResultsController];
               
            };
            if (!success) NSLog( @"couldnt open document");
        }];
    }
   
}

- (void)viewWillAppear:(BOOL)animated {
      [self doFetchResultsController];
}

- (void) doFetchResultsController {
    
    if (self.appDelegate.document.documentState == UIDocumentStateNormal) {
        NSManagedObjectContext *context = self.appDelegate.document.managedObjectContext;
        NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Person"];
        
        [request setRelationshipKeyPathsForPrefetching:
        [NSArray arrayWithObject:@"hasDeeds"]];
        
        NSSortDescriptor *sortByName = [NSSortDescriptor sortDescriptorWithKey:@"firstName"  ascending:YES];
        request.sortDescriptors = @[sortByName];

        
        self.fetchedResultsController = [[NSFetchedResultsController alloc]
                                              initWithFetchRequest:request
                                              managedObjectContext:context
                                              sectionNameKeyPath:nil
                                              cacheName:nil];
    
    
         NSError *error;
         BOOL success = [self.fetchedResultsController performFetch:&error];
        [self.tableView reloadData];
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
       // [destController setDelegate:self];
        [destController setTitle:@"Add person "];
        [destController setExistingPersons:self.appDelegate.personArray];
    }
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
 //   return [[self.fetchedResultsController sections] count];
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [[[self.fetchedResultsController sections] objectAtIndex:section] numberOfObjects];
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Person";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    Person* person = [self.fetchedResultsController objectAtIndexPath:indexPath];
    
    NSArray *deeds = [[person hasDeeds] allObjects];
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
