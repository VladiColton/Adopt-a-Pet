$(document).ready(function()
{
    /* smooth scrolling for scroll to top */
    $('#to-top').bind('click', function()
    {
        $('body,html').animate({scrollTop: 0}, 2500);
    });

    //Easing Scroll replace Anchor name in URL and Offset Position
    $(function()
    {
        $('a[href*=#]:not([href=#])').click(function()
        {
            if (location.pathname.replace(/^\//,'') === this.pathname.replace(/^\//,'') && location.hostname === this.hostname) 
            {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
                if (target.length) 
                {
                        $('html,body').animate({scrollTop: target.offset().top -420}, 3500, 'easeOutBounce');
                        return false;
                }
            }
        });
    });
    
    $(".dropdown").hover(            
        function() {
            $('.dropdown-menu', this).not('.in .dropdown-menu').stop(true,true).slideDown("400");
            $(this).toggleClass('open');        
        },
        function() {
            $('.dropdown-menu', this).not('.in .dropdown-menu').stop(true,true).slideUp("400");
            $(this).toggleClass('open');       
        }
    );
    
    // Closes the sidebar menu on menu-close button click event
    $("#menu-close").click(function(e)	//declare the element event ...'(e)' = event (shorthand)
    {
        // - will not work otherwise")
        $("#sidebar-wrapper").toggleClass("active");//instead on click event toggle active CSS element
        e.preventDefault(); //prevent the default action
    });

    // Open the Sidebar-wrapper on Hover
    $("#menu-toggle").hover(function(e)//declare the element event ...'(e)' = event (shorthand)
    {
        $("#sidebar-wrapper").toggleClass("active",true);//instead on click event toggle active CSS element
        e.preventDefault();//prevent the default action
    });

    $("#menu-toggle").bind('click',function(e)//declare the element event ...'(e)' = event (shorthand)
    {
        $("#sidebar-wrapper").toggleClass("active",true);//instead on click event toggle active CSS element
        e.preventDefault();//prevent the default action
    });

    $('#sidebar-wrapper').mouseleave(function(e)//declare the jQuery: mouseleave() event 
    {
        /* toggleClass: Add or remove one or more classes from each element
        in the set of matched elements, depending on either the class's
        presence or the value of the state argument */
        $('#sidebar-wrapper').toggleClass('active',false);
        e.stopPropagation();//Prevents the event from bubbling up the DOM tree (//api.jquery.com/event.stopPropagation/' for details)
        e.preventDefault();// Prevent the default action of the event will not be triggered
    });
});

function openFullAnimalDescription(index)
{
    let id="overlay"+index.toString();
    document.getElementById(id).style.display = "block";
}
function closeFullAnimalDescription(index) 
{
    let id="overlay"+index.toString();
    document.getElementById(id).style.display = "none";
}

//Google maps API Callback function
var globalAnimalCardIndexSelected = 0;
var maps = [];
var oneTimeSetForOurLocation = true;
function myMap()
{
    globalAnimalCardIndexSelected++;
    var mapProp ={
        center:new google.maps.LatLng(51.508742,-0.120850),
        zoom:5,
        mapTypeControl: false,
        streetViewControl: false,
        fullscreenControl: false,
        zoomControl: false
    };
    for(var i = 0, length = globalAnimalCardIndexSelected; i < length; i++)
    {
        maps[i] = new google.maps.Map(document.getElementById("googleMap"+i),mapProp);
        let geocoder = new google.maps.Geocoder();
        var userLocation = $("#addressGeoLocation"+i).text();
        if (userLocation === "" || userLocation === null)
            userLocation = 'Israel'; //Set default geoLocation to avoid errors
        geocodeAddress(geocoder, maps[i], userLocation);
    }
    
    //Only one time set for our-location window

        var geocoder2 = new google.maps.Geocoder();
        var map2= new google.maps.Map(document.getElementById("googleMapOurLocation"),mapProp);
        geocodeAddress(geocoder2, map2, null);

}

function geocodeAddress(geocoder, resultsMap, geoAddress) 
{
    var address = (geoAddress != null ? geoAddress : 'Haifa, Israel'); //document.getElementById('address').value;
    geocoder.geocode({'address': address}, function(results, status) {
      if (status === 'OK') {
        resultsMap.setCenter(results[0].geometry.location);
        var marker = new google.maps.Marker({
          map: resultsMap,
          position: results[0].geometry.location
        });
      } else {
        alert('Geocode was not successful for the following reason: ' + status);
      }
    });
}

function updateSelectedAnimalFields(animalName, animalAge, animalType, animalBreed, animalDescription, animalID)
{
    console.log("In updateSelectedAnimalFields with params:" + "Name="+animalName + " Age="+animalAge + " Type="+animalType);
    var animalNameField = document.getElementsByClassName("form-control-custom animalUpdateFormField_Name");
    var animalAgeField = document.getElementsByClassName("form-control-custom animalUpdateFormField_Age");
    var animalTypeField = document.getElementsByClassName("form-control-custom animalUpdateFormField_Type");
    var animalBreedField = document.getElementsByClassName("form-control-custom animalUpdateFormField_Breed");
    var animalDescriptionField = document.getElementsByClassName("form-control-custom animalUpdateFormField_Description");
    var animalIdField = document.getElementsByClassName("text-primary animalUpdateFormField_ID");
    
    //Set the details for the form.
    animalNameField[0].value = animalName;
    animalAgeField[0].value = animalAge;
    animalTypeField[0].value = animalType;
    animalBreedField[0].value = animalBreed;
    animalDescriptionField[0].value = animalDescription;
    
    if(animalID === '')
        animalID = -1;
    animalIdField[0].innerHTML = animalID;
    animalIdField[1].value = animalID;
}