## Thought Process.
* An Address book is just a container for contacts.
* Contacts can be of type LANDLINE,HOME,MOBILE, which is provisioned,but used hardcoded landline due to lack of time.
* Address book can have multiple contacts
* Contacts can belong to multiple addressbook. @ManyToMany

* PhoneDetails and personalInfo do not exist without contact , hence marked them as embedded info.
* Contact is the owner of addressbook in terms of technical implementation. This is because when someone list all addressbook then I did not want to display all contacts too.
* Contact display are paginated and sorted.
* When displaying merged contacts. Duplicates are removed based on hashset. Also Contacts belonging to different sets are merged in 1 set and displayed. Phonedetails have equals and hashcode implementation by Lombok.

## Models for DB
* AddressBook - Pure Addressbook with name.
* Contact.java - Contact details with PersonalInfo and PhoneInfo embedded type
* Enum for PhoneType.

## Models for Core.
* The Above Models are duplicated in the core with same names. 
* These are for DTOs as I did not want to leak db objects to the Rest api layer.
* Use Dozzer at Core layer to do the mapping from db opbjects to data objects.



## Modules in the project
* address-book-app  -- This is the parent project. All dependencies which need to be inherited are defined in build.gradle
* address-book-core -- this is the business module which will always evolve. All core services which does business logic is here.
* address-book-db  -- This is the database module. USes H2 in memory for testing
* address-book-rest -- This is the REST api module.
