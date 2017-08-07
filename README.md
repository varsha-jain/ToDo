# Pre-work - Easy Task-ToDo List

Easy Task-ToDo List is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: VARSHA JAIN

Time spent: 9 hours spent in total

## User Stories

The following **required** functionality is completed:

* [Y ] User can **successfully add and remove items** from the todo list
* [Y ] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [ Y] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [Y ] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [Y ] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ Y] Add support for completion due dates for todo items (and display within listview item)
* [Y ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [Y ] Add support for selecting the priority of each todo item (and display in listview item)
* [Y ] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [ Y] List anything else that you can get done to improve the app functionality!
I am working on the app to add the feature of Notification as well as archiving all the tasks or synching it with cloud. I have also thought of adding the feature to categorize each task as personal, work or others.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/TOICisK.gif' title='Video Walkthrough' width='600' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** The android app development platform is higly user friendly. Earlier I developed android apps using Eclipse IDE. Aandroid studio is much better than eclipse when it comes to android app development as it is more user friendly. UI customization is easy in android studio as compared to eclipse.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** The array adapter used in my pre-work displays the list of tasks added by the user. The list of tasks is retrieved from the database and displayed on a list view. It's function in android is that it acts as a bridge between UI component and data source. Adapter eases out the process of displaying data on various UI component such as List View, Spinner or Grid View. convertView is used to recycle the old view. The adapter populates each list item with a View object by calling getView() on each row. 
The Adapter uses the convertView as a way of recycling old View objects that are no longer being used.

## Notes

Describe any challenges encountered while building the app.

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
