# TODO app

obeythetestinggoat book's TODO app for KMP.
Let's see what interesting analogies can we discover between a Compose app with a Web app along
the way.

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

## TODO

- [x] Setup Cucumber
  - https://www.kodeco.com/26211276-getting-started-with-cucumber
  - https://github.com/realpacific/cucumber-spring-boot-kotlin/blob/main/build.gradle.kts
  - https://github.com/cucumber/cucumber-jvm-starter-gradle-java
- [x] Run the functional test in [Chapter 2](https://www.obeythetestinggoat.com/book/chapter_02_unittest.html).
