package com.urlStatus.url.connection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebCrawlerController {

    @GetMapping("/")
    public String showForm() {
        return "index"; // Return the HTML form page (index.html)
    }

    @PostMapping("/analyze")
    public String analyzeWebPage(@RequestParam("url") String url, Model model) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            // Use Jsoup to connect to the given URL and parse the HTML
            Document doc = Jsoup.connect(url).get();
            // Get the page title
            String pageTitle = doc.title();
            model.addAttribute("pageTitle", pageTitle);
            // Get all links on the page
            Elements links = doc.select("a[href]");
            List<String> linkList = new ArrayList<>();

            for (Element link : links) {
                // Get the link text and href attribute
                String linkText = link.text();
                String href = link.attr("href");
                // Add the link text to the list
                linkList.add(linkText + " - " + href);
            }
            model.addAttribute("links", linkList);

        } catch (IOException e) {
            // Handle any exceptions that may occur when fetching the URL
            model.addAttribute("error", "Error fetching URL: " + e.getMessage());
        }
        return "result"; // Return the HTML result page (result.html)
    }
}
