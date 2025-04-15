const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  /*
  devServer: {
    proxy: {
      '/category/create': {
        target: 'http://localhost:8092', // Backend server URL
        changeOrigin: true, // Ensures the host header matches the target
        ws: true, // Proxy websockets if needed
        pathRewrite: { '^/category/create': '/category/create' }, // Optional: Preserve exact path
      }
    }
  }
  */

});
