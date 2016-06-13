var tweetData = [];
var filter = "";

$(document).ready( function() {
    $("#filter").keyup(function() {
        filter = $("#filter").val();
        processTweets(tweetData);
    });
    generatePage()
});



function generatePage() {
    getTweets();
    window.setInterval(resetTweets,60000);
}

function resetTweets() {
    getTweets();
}

function clearTweets() {
    $(".tweets").empty();
}

function getTweets() {
    $.ajax({
        url: "http://localhost:8080/twitter/getTweets/salesforce/10"
    }).then(processTweets);
}

function processTweets(data) {
    tweetData = data;
    printTweets(data);
}

function printTweets(data) {
    clearTweets();
    data.forEach(printIfFilter);
}

function printIfFilter(item) { 
    console.log("Filter here: " + filter);
    if (item.text.includes(filter)) {
        printTweet(item);
    }
}

function printTweet(item) {
    var tablePrefix  = "<table class='tweet'><tr><th>";
    var img = "<img class= 'image', src="+item.user.profile_image_url+">";
    var imgSuffix = "</th><th class = 'nameText'>";
    var imageSection = tablePrefix +  img+ imgSuffix;
    var screenName = "<div class = 'screenName'>@" + item.user.screen_name + "</div>";
    
    var retweets = "<div class = 'retweet'>Retweets</div>";
    
    var retweetCount = "<div class = 'retweetCount'>"+item.retweet_count + "</div>";
    
    var thingToWrite = imageSection +  item.user.name  + screenName +  "</th><th>" + retweetCount + retweets + "</th></tr><tr><td colspan='3'>" + item.text + "</td></tr></table>";

    $(".tweets").append(thingToWrite);
}

function formChanged() {
    filter = $("#filter").val();
    clearTweets();
    processTweets(tweetData);
}
