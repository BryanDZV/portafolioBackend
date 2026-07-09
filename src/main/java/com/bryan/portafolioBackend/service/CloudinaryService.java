package com.bryan.portafolioBackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service //con esto se dice que es un servicio que tine logica
public class CloudinaryService {
    @Autowired //esta anotacion permite inyectar dependencias de manera automática
    private Cloudinary cloudinary;//esto sirve para poder usar los metodos de cloudinary en este servicio
    public String uploadImage(MultipartFile file)throws IOException {
        // 1. cloudinary.uploader().upload(...) es el método mágico que envía la foto a la nube.
        // 2. file.getBytes() convierte la imagen en datos puros para poder enviarla por internet.
        // 3. ObjectUtils.emptyMap() le dice a Cloudinary que no queremos ponerle configuraciones extra (como recortes).
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        // Cloudinary nos responde con un "Map" (un diccionario de datos).
        // A nosotros solo nos interesa sacar la URL segura (https) de la imagen.
        return uploadResult.get("secure_url").toString();
    }

}
