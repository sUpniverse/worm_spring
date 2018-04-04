package com.javalec.spring_pjt_board_command;

import org.springframework.ui.Model;

public interface BCommand {
	public void execute(Model model);
}
