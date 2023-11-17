package jaime.funkoext2.FunkoyCategorias.Config.Storage;

import jaime.funkoext2.FunkoyCategorias.storage.Services.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
    @Configuration
    public class StorageConfig {
        private final StorageService storageService;

        @Value("${upload.delete}")
        private String deleteAll;

        @Autowired
        public StorageConfig(StorageService storageService) {
            this.storageService = storageService;
        }

        @PostConstruct
        public void init() {
            if (deleteAll.equals("true")) {
                storageService.deleteAll();
            }

            storageService.init();
        }
    }

