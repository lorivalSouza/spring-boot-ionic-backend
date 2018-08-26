package com.lorival.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.lorival.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
}
