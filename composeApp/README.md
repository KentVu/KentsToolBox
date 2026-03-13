# TODO app

obeythetestinggoat book's TODO app for KMP.
Let's see what interesting analogies can we discover between a Compose app with a Web app along
the way.

To closely follow the book (i.e. Make codes in this app resemble codes in the book)
as much as possible, I've structure this app to mimic a HTTP-like transaction, the "Backend"
serves as a "local" server and drives the UI via "Model" class, 
Think of "Model" like "template variables", while
Composable UI like Django templates.

## Chapter 3. Testing a Simple Home Page with Unit Tests

> [...] Django was with any web server, Django’s main job is to decide what to do when a user asks for a particular URL on our site.[...]

→ Need to use Navigation library.

### Unit Testing a View

We can use something like `runComposeUiTest { setContent { <content> } }` instead.
Instead of searching HTML content we can make assertions against the Semantic tree.

→ Does that mean our runComposeUiTest considered Unit test?
  → Maybe yes. 

- Is the title what's inside TopAppBar or the actual desktop's Window title?
  - → Since we're supporting mobile app also, the window's title bar seems irrelevant!

- We can only check the semantic tree, that means we can't further verify whether the text is inside
  the TopAppBar or just in a `Text()` composable!

## Chapter 4: What Are We Doing with All These Tests? (And, Refactoring)

```python
        # She is invited to enter a to-do item straight away
        inputbox = self.browser.find_element(By.ID, "id_new_item")  
        self.assertEqual(inputbox.get_attribute("placeholder"), "Enter a to-do item")
```
We shouldn't abuse testTag in Compose production code?
- https://proandroiddev.com/stop-using-test-tags-in-the-jetpack-compose-production-code-b98e2679221f
- https://stackoverflow.com/questions/78730330/best-practices-for-using-testtag-in-jetpack-compose

The workaround disables ability to pass parameter to step function :(

"New to-do item did not appear in table"'s error in Compose:

```
Failed to assert the following: (hasAnyChildThat(Text + InputText + EditableText contains '1: Buy peacock feathers' (ignoreCase: false)))
Semantics of the node:
Node #32 at (l=512.0, t=120.0, r=512.0, b=120.0)px, Tag: 'id_list_table'
Has 2 siblings
Selector used: (TestTag = 'id_list_table')
```
→ Okay maybe we don't need to add further error description as Compose test is pretty specific!

## Chapter 5: Saving User Input: Testing the Database

Let's explore core differences between web app and native app:
- There are no request/response, (i.e. server), access the database directly (SQLite, Filesystem I/O etc)

> How about abstracting the server using HTTP?, 

→ hmm.. not practical it seems.. Just vanilla API calls are enough!, the important thing is can we
isolate it in a module!

### Simulating an HTML form's submit

- It blocks until page reload and displays page's updates.
- It sends data to the serer and re-render what the server returns.

So what the backend should do?:
- Return data to display the screen.
- It don't care what the screen will display, that's what Compose's responsibility.

CSRF Token? No need to care because everything is run locally.

### GET and POST?

Since we are in a strongly typed language Kotlin, the data passed between frontend and backend is also strongly-typed (a.k.a. data classes).
So there's no need for form-data dictionary key check etc.

### Django view.render.template?

That's my `backend.model`!!

### Skipped 5.12. Creating Our Production Database with migrate

Production db? Migrate? (it's different for _Room_ )

## TODO

- [x] Run the functional test in [Chapter 2](https://www.obeythetestinggoat.com/book/chapter_02_unittest.html).
- [ ] Focus on id_new_item upon app start.
- [ ] Code smell: POST test is too long?
