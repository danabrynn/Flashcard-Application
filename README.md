# Personal Project - Flashcard Creator

## Project Overview

This application will allow users to create decks of flashcards to store information
they want to study. For example, this application could be used by students to:
- create a flashcard deck of Spanish vocabulary words, 
with the English definitions on the card's flip side
- create a deck of the amino acid names and their corresponding
chemical formula for a biochemistry course

Users will then be able to *view cards* from their decks, *flipping* cards to
see their corresponding definitions/values, and *marking* whether they arrived at the 
card's value successfully. Statistics for deck completion/ correctness will be shown.

This project is of interest to me, as flashcards engage active recall, and are one of 
the more effective methods of memorizing information. This application should give
students a tool to be more involved with and personalize their learning.

## User Stories

- As a user, I want to be able to add a card to my deck
- As a user, I want to be able to view a list of cards in my deck
- As a user, I want to be able to remove a card from my deck
- As a user, I want to be able to flip cards in my deck and mark if they were completed successfully
- As a user, I want to see statistics on my deck completion
- As a user, I want to have the option to save my deck to file
- As a user, I want to have the option to load my deck from file

## Instructions for Grader

- You can generate the first required action related to the user story "adding multiple Xs to a Y" by 
clicking the 'Add Card' button. All cards in the deck will be visible on the screen if the 'Show Cards' 
checkbox is selected.
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by 
selecting the checkbox next to any cards you want to delete and clicking the 'Delete Card' button.
- You can locate my visual component by loading the application - a logo is present on the opening window 
and is also present beside the deck name in the main window.
- You can save the state of my application by clicking either the 'Save As' or 'Save' card button and entering 
a file name if it hasn't already been stored.
- You can reload the state of my application by clicking 'Load deck from file' button on opening or by 
clicking the "Load Deck" button from the main application window, and then entering the file path.

## Phase 4: Task 2
### Representation sample of console logged events that occur during program run
Thu Nov 30 12:19:48 PST 2023  
Added Flashcard to Flashcard Demo Deck  
Flashcard ID number: 1  
Front: What is the national animal of Scotland?  
Back: the Unicorn  

Thu Nov 30 12:20:35 PST 2023  
Added Flashcard to Flashcard Demo Deck  
Flashcard ID number: 2  
Front: How many stars are in the Milky Way?  
Back: 100 billion  

Thu Nov 30 12:21:01 PST 2023  
Flipped 2 of 2 cards in Flashcard Demo Deck  

Thu Nov 30 12:21:17 PST 2023  
Deleted Flashcard from Flashcard Demo Deck  
Flashcard ID number: 1  
Front: What is the national animal of Scotland?  
Back: the Unicorn  

## Phase 4: Task 3
Several changes could be made to improve the design of the project. It may be beneficial to separate some of 
the code in the FlashcardApp class into another classes to improve cohesion, as at the moment the class is 
responsible for performing multiple distinct tasks. However, there might be tradeoffs, as this could 
result in increased coupling. In particular, extracting a class responsible for handling user input via 
JOptionPanes would be beneficial, as this is really a distinct task, and at the moment there is fairly 
repetitive code in both the FlashcardApp and FlashcardGame classes responsible for handling input.

It would also likely be beneficial to use the Observer Pattern to notify various components of the GUI when 
changes are made to the deck. This would allow various panels and components of the user interface to update 
more easily in response to deck changes and might reduce coupling. It would also likely simplify and 
improve the readability of the code, as at the moment whenever changes are made, calls are made directly to 
update each of the interested panels. In general, the code could likely be refactored to reduce coupling, as 
currently multiple classes have references to both the deck and to a list of all the flashcards in the deck.