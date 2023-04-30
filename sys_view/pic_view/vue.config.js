const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = {
  //...
  devServer: {
    allowedHosts: [
      'atri.wiki', // 允许访问的域名地址，即花生壳内网穿透的地址
      '.atri.wiki'   // .是二级域名的通配符
    ],
  },
};


