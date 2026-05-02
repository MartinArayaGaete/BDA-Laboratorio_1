package com.example.demo.dtos;

import java.util.List;

public class PuntajeRondaDTO {
    private Long idRonda;
    private Long idParticipacion;
    private List<Integer> flechas;
    private Long idAdmin;

    public PuntajeRondaDTO() {
    }

    public PuntajeRondaDTO(Long idRonda, Long idParticipacion, List<Integer> flechas, Long idAdmin) {
        this.idRonda = idRonda;
        this.idParticipacion = idParticipacion;
        this.flechas = flechas;
        this.idAdmin = idAdmin;
    }

    // Getters y Setters
    public Long getIdRonda() { 
        return idRonda; 
    }
    
    public void setIdRonda(Long idRonda) { 
        this.idRonda = idRonda; 
    }

    public Long getIdParticipacion() { 
        return idParticipacion; 
    }
    
    public void setIdParticipacion(Long idParticipacion) { 
        this.idParticipacion = idParticipacion; 
    }

    public List<Integer> getFlechas() { 
        return flechas; 
    }
    
    public void setFlechas(List<Integer> flechas) { 
        this.flechas = flechas; 
    }

    public Long getIdAdmin() { 
        return idAdmin; 
    }
    
    public void setIdAdmin(Long idAdmin) { 
        this.idAdmin = idAdmin; 
    }
}