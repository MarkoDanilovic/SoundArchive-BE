package com.example.soundarchive.config;

//@Configuration
public class CorsConfig {//implements WebMvcConfigurer {

//    @Value("${conf.cors.allowed-origins}")
//    private String[] allowedOrigins;
//
//    @Value("${conf.cors.allowed-methods}")
//    private String[] allowedMethods;
//
//    @Value("${conf.cors.allowed-headers}")
//    private String[] allowedHeaders;
//
//    @Value("${conf.cors.exposed-headers}")
//    private String[] exposedHeaders;
//
//    public CorsConfig() {}
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(Arrays.asList(this.allowedOrigins));
//        config.setAllowedMethods(Arrays.asList(this.allowedMethods));
//        config.setAllowedHeaders(Arrays.asList(this.allowedHeaders));
//        config.setExposedHeaders(Arrays.asList(this.exposedHeaders));
//
//        registry
//                .addMapping("/api/**")
//                .allowedOrigins(this.allowedOrigins)
//                .allowedMethods(this.allowedMethods)
//                .exposedHeaders(this.exposedHeaders)
//                .allowedHeaders(this.allowedHeaders);
//    }
}
