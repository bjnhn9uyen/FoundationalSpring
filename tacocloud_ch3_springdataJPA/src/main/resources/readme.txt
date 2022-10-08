no need schema.sql here because Hibernate will create schema automatically base on @Entity at the boot time.
put Hard-coded data of Ingredient into @Bean CommandLineRunner in TacoCloudApplication, 
Spring Boot will execute that @Bean data at the boot time.