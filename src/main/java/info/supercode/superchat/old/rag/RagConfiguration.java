package info.supercode.superchat.old.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    @Value("vectorstore.json")
    private String vectorStoreName;

    @Value("classpath:/docs/olympic-faq.txt")
    private Resource resource;


//    @Bean
//    public VectorStore mongodbVectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
//        var vectorStore = MongoDBAtlasVectorStore.builder(mongoTemplate, embeddingModel)
//                .collectionName("custom_vector_store")
//                .vectorIndexName("custom_vector_index")
//                .build();
//        if((resource.getFilename().toLowerCase()).endsWith(".pdf")) {
//            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource, PdfDocumentReaderConfig.builder()
//                    .withPageTopMargin(0)
//                    .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
//                            .withNumberOfTopTextLinesToDelete(0)
//                            .build())
//                    .withPagesPerDocument(1)
//                    .build());
//            List<Document> documents =  pdfReader.read();
//            TextSplitter textSplitter = new TokenTextSplitter();
//            List<Document> splitDocuments = textSplitter.apply(documents);
//            vectorStore.add(splitDocuments);
//        } else if((resource.getFilename().toLowerCase()).endsWith(".txt")) {
//            TextReader textReader = new TextReader(resource);
//            textReader.getCustomMetadata().put("filename", "olympic-faq.txt");
//            List<Document> documents = textReader.read();
//            TextSplitter textSplitter = new TokenTextSplitter();
//            List<Document> splitDocuments = textSplitter.apply(documents);
//            vectorStore.add(splitDocuments);
//        }
//        return vectorStore;
//    }

//    @Bean
//    VectorStore simpleVectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) throws IOException {
//
//        //var vectorStore = new SimpleVectorStore(embeddingModel);
//        var vectorStore = SimpleVectorStore.builder(embeddingModel).build();
//
//        var vectorStoreFile = getVectorStoreFile();
//        if (vectorStoreFile.exists()) {
//            log.info("Vector Store File Exists,");
//            vectorStore.load(vectorStoreFile);
//        } else {
//            log.info("Vector Store File Does Not Exist, loading documents");
//            createVectorStoreFile();
//
//            if((resource.getFilename().toLowerCase()).endsWith(".pdf")) {
//                PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource, PdfDocumentReaderConfig.builder()
//                        .withPageTopMargin(0)
//                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
//                                .withNumberOfTopTextLinesToDelete(0)
//                                .build())
//                        .withPagesPerDocument(1)
//                        .build());
//                List<Document> documents =  pdfReader.read();
//                TextSplitter textSplitter = new TokenTextSplitter();
//                List<Document> splitDocuments = textSplitter.apply(documents);
//                vectorStore.add(splitDocuments);
//                vectorStore.save(vectorStoreFile);
//            } else if((resource.getFilename().toLowerCase()).endsWith(".txt")) {
//                TextReader textReader = new TextReader(resource);
//                textReader.getCustomMetadata().put("filename", "olympic-faq.txt");
//                List<Document> documents = textReader.read();
//                TextSplitter textSplitter = new TokenTextSplitter();
//                List<Document> splitDocuments = textSplitter.apply(documents);
//                vectorStore.add(splitDocuments);
//                vectorStore.save(vectorStoreFile);
//            }
//
//        }
//        return vectorStore;
//    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);
    }

    private void createVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        // Create a File object
        File file = new File(absolutePath);
        try {
            // Check if the file already exists
            if (file.exists()) {
                System.out.println("File already exists: " + absolutePath);
            } else {
                // Attempt to create the file
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    // Attempt to create the directory
                    if (parentDir.mkdirs()) {
                        System.out.println("Directory created: " + parentDir.getAbsolutePath());
                    } else {
                        System.err.println("Failed to create the directory.");
                    }
                }
                if (file.createNewFile()) {
                    System.out.println("File created: " + absolutePath);
                } else {
                    System.out.println("Failed to create the file.");
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
        }
    }

}
