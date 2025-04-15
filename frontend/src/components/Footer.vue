<template>
  <footer class="footer">
    <div class="container pt-5">
      <div class="row">

        <div v-for="(section, index) in footerSections"
             :key="index"
             class="col-md-3 col-6"> <!--medium: display 12/3 = 4 sections, small: 12/6= 2 sections -->
          <ul class="footer-list">
            <li class="footer-title">{{ section.title }}</li>
            <li v-for="(link, linkIndex) in section.links" :key="linkIndex">
              <a class="footer-link" :href="link.url">{{ link.text }}</a>
            </li>
          </ul>

        </div>
      </div>
    </div>
  </footer>
</template>
<script>
export default {
  data() {
    return {
      footerSections: [
        {
          title: "Get to know us",
          links: [
            { text: "About us", url: "#" },
            { text: "Android app", url: "#" },
            { text: "iOS app", url: "#" },
          ]
        },
        {
          title: "Connect with us",
          links: [
            { text: "Facebook", url: "#" },
            { text: "Twitter", url: "#" },
            { text: "Instagram", url: "#" }
          ]
        },
        {
          title: "Make money with us",
          links: [
            { text: "Sell on our platform", url: "#" },
            { text: "Affiliate program", url: "#" },
            { text: "Advertise", url: "#" }
          ]
        },
        {
          title: "Let us help you",
          links: [
            { text: "Help Center", url: "#" },
            { text: "Support", url: "#" },
            { text: "Contact us", url: "#" }
          ]
        }
      ]
    };
  }
};
</script>
<style>
.footer {
  margin-top: 100px;
  background-color: #232F3E;
  font-size: 16px;
  padding: 20px 0;
}

.footer-list {
  list-style-type: none;
  padding: 0;
}

.footer-title {
  color: #fff;
  font-weight: bold;
  padding-bottom: 10px;
}

.footer-link {
  color: #DDD;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-link:hover {
  color: #f0a500;
}

li {
  padding-bottom: 10px;
}


</style>