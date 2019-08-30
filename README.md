# Slides
This project is a library (and example app) made to simplify the process of creating educational Android apps. 
A list of lessons or tasks called "slides" can be compiled in a JSON file, and loaded into a set of activities for displaying their contents.
Slides can be simple, non-interactive content such as text or HTML, or they can be richer interactive content such as a text editor,
a spreadsheet, or other course-specific interfaces. While only a few slides are provided by default, new custom slides can be added and
utilized by the library.

"slides_lib" contains the core functionality of the project, and "slides_app" is an example app demonstrating how to use the library.

## Default Slides
The following four types of slides are included with slides_lib:

* Text Slide (TextSlide)
...The text slide displays the contents in a TextView, and has no interactive features
* HTML Slide (HtmlSlide)
...The HTML slide displays the contents of the slide as HTML with a WebView. Use of .html, .css and .js files is not supported for this slide.
* HTML File Slide (HtmlFileSlide)
...The HTML file slide is similar to the HTML slide, but instead of displaying the contents as HTML, the contents of an HTML file slide is the name of an HTML file in its own directory under assets/web. The name of the directory containing this HTML file must have the same name as said file, but the .html extension must be replaced with \_files. For example, an HTML slide whose contents are "mypage.html" requires that there be an HTML file called "mypage.html" located in assets/web/mypage_files. Other files for the page can be included in the directory containing the HTML file, and the base URL for the page will be this directory.
* Document Slide (DocSlide)
...The document slide displays the contents in a TextView, and has an area for the user to enter text. Text entered into this field is persisted into a key/value store, and the key for said store is provided by the "persistedDataKey" field. This field can be used in other slides to access the same persisted text.
