package Cinema;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import UserItem.IUserItem;
import javax.swing.border.BevelBorder;
public class Form {
	private static final String[] GENRELIST = new String[]{"�A�N�V����", "�`��", "�A�j���[�V����", "�`�L", "�R���f�B", "�ƍ�", "�h�L�������^���[", "�h���}", "�t�@�~���[", "�t�@���^�W�[", "�t�B�����m���[��", 
			"���j", "�z���[", "���y", "�~���[�W�J��", "�~�X�e���[", "���}���X", "SF", "�Z��", "�X�|�[�c", "�X�����[", "�푈", "������"};
	private static final String[] COLUMNNAMES = new String[]{"ID","�^�C�g��","�L�^��"};
	private static final String SEPARATE = "/";
	private static final String APPNAME = "CinemaBook ";
	private static final Font LABEl_FONT = new Font("MS P�S�V�b�N", Font.PLAIN, 12);
	private static final Color LABEL_FORECOLOR = Color.WHITE;
	private static final Font MENUITEM_FONT = new Font("MS P�S�V�b�N", Font.PLAIN, 13);
	
	private JFrame frame;
	private JTextField textField_Title;
	private JTextField textField_Director;
	private JTextField textField_Actor;
    private JTextArea textArea_Comment;
	private JComboBox<String> comboBox_Genre;
	private JSlider slider_Review;
	private JLabel label_Review;
	private JTextField textField_Playwright;
	private JButton button_Add;
	private JButton button_Delete;
	private JSpinner spinner_Year;
	private JSpinner spinner_Month;
	private JSpinner spinner_Date;
	private JButton button_Save;
	private JButton button_Edit;
	private JLabel label_ImageView;
	
	private CinemaBook cinemaBook;
	private Converter converter;
	private File filePath;
	private BufferedImage image;
	private DefaultTableModel tableModel;
	private JTable table;
	
	public static void main(String[] args) {
		Form form = new Form();
		form.frame.setVisible(true);
	}
	
	private void initializeComponentValue() { //���̓R���|�[�l���g�̒l������������
		textField_Title.setText("");
		textField_Director.setText("");
		textField_Actor.setText("");
		textField_Playwright.setText("");
		textArea_Comment.setText("");
		comboBox_Genre.setSelectedIndex(0);
		slider_Review.setValue(1);
		label_ImageView.setIcon(new ImageIcon());
		label_ImageView.setText("�N���b�N���Ă�������");
		image = null;
	}

	private IUserItem getItemIncludedComponentValue() { //���̓R���|�[�l���g�̒l��������IUserItem��Ԃ�
		IUserItem item = new CinemaItem(CinemaItem.COLUMNNAME_ARRAY);
		String title = textField_Title.getText();
		String director = textField_Director.getText();
		String actor = textField_Actor.getText();
		String playwright = textField_Playwright.getText();
		String genre = comboBox_Genre.getSelectedItem().toString();
		int review = slider_Review.getValue();
		String comment = textArea_Comment.getText();
		int year = Integer.valueOf(spinner_Year.getValue().toString());
		int month = Integer.valueOf(spinner_Month.getValue().toString());
		int date = Integer.valueOf(spinner_Date.getValue().toString());
		String day = year + SEPARATE + month + SEPARATE + date;
		
		item.putColumnValue(CinemaItem.TITLE, title);
		item.putColumnValue(CinemaItem.DIRECTOR, director);
		item.putColumnValue(CinemaItem.ACTOR, actor);
		item.putColumnValue(CinemaItem.PLAYWIGHT,playwright);
		item.putColumnValue(CinemaItem.GENRE, genre);
		item.putColumnValue(CinemaItem.COMMENT, comment);
		item.putColumnValue(CinemaItem.REVIEW, review);
		item.putColumnValue(CinemaItem.DATE, day);
		item.putColumnValue(CinemaItem.IMAGE, image);
		
		return item;
	}
	
	private boolean isFulledItemValue(IUserItem item) { //IUserItem�̍��ڂ����ׂē��͂���Ă��邩���ׂ�B��ł��s���������false��Ԃ��A�Ȃ����true��Ԃ�
		for(int index = 0; index < item.getColumnIndex(); index++) {
			if(item.getColumnValue(index).equals("") || image == null) {
				return false;
			}
		}
		return true;
	}
	
	private void setComponentEnable(boolean enable) { //���̓R���|�[�l���g��Enable�̐؂�ւ�
		textField_Title.setEditable(enable);
		textField_Director.setEditable(enable);
		textField_Actor.setEditable(enable);
		textField_Playwright.setEditable(enable);
		textArea_Comment.setEditable(enable);
		comboBox_Genre.setEnabled(enable);
		slider_Review.setEnabled(enable);
		spinner_Year.setEnabled(enable);
		spinner_Month.setEnabled(enable);
		spinner_Date.setEnabled(enable);
	}
	
	private enum OperationMode{ //CinemaBook�̃��[�h��o�^���Ă���
		Add,Delete,View,Select,Edit,SaveEdit,SaveNewItem,Search
	}
	
	private OperationMode operation;
	public void operationModeChange(OperationMode operationMode) { //CinemaBook�̃��[�h�ύX���������Ƃ��̏���
		operation = operationMode;
		int selectRowIndex = table.getSelectedRow();
		int idValue = -1;
		if(selectRowIndex != -1){
			idValue = Integer.valueOf(tableModel.getValueAt(selectRowIndex, 0).toString());
		}
		switch(operation) {
			case Add: { //�ǉ����[�h
				initializeComponentValue();
				button_Add.setEnabled(false);
				button_Delete.setEnabled(false);
				button_Edit.setEnabled(false);
				button_Save.setEnabled(true);
				table.setEnabled(false);
				setComponentEnable(true);
				frame.setTitle(APPNAME + "�ǉ����[�h");
				break;
			}
			case Delete: { //�I�������A�C�e�����폜����
				if(selectRowIndex != -1) {
					tableModel.removeRow(selectRowIndex);
					IUserItem item = (CinemaItem) cinemaBook.removeItem(idValue);
					JOptionPane.showMessageDialog(frame, "�A�C�e�����폜���܂����B\n" + item.toString(),"�폜",JOptionPane.INFORMATION_MESSAGE);
					initializeComponentValue();
				}
				break;
			}
			case View: { //�ϗ����[�h
				initializeComponentValue();
				button_Save.setEnabled(false);
				button_Edit.setEnabled(false);
				button_Delete.setEnabled(false);
				button_Add.setEnabled(true);
				table.setEnabled(true);
				setComponentEnable(false);
				frame.setTitle(APPNAME + "�ϗ����[�h");
				break;
			}
			case Edit: { //�ҏW���[�h
				button_Add.setEnabled(false);
				button_Delete.setEnabled(false);
				button_Edit.setEnabled(false);
				button_Save.setEnabled(true);
				table.setEnabled(false);
				setComponentEnable(true);
				frame.setTitle(APPNAME + "�ҏW���[�h");
				break;
			}
			case Select: { //�A�C�e���I�����̏����@�I�������A�C�e���̏����e�R���|�[�l���g�ɕ\������
				if(selectRowIndex != -1) {
					IUserItem item = (CinemaItem) cinemaBook.getItem(idValue);
					textField_Title.setText(item.getColumnValue(CinemaItem.TITLE).toString());
					textField_Director.setText(item.getColumnValue(CinemaItem.DIRECTOR).toString());
					textField_Actor.setText(item.getColumnValue(CinemaItem.ACTOR).toString());
					textField_Playwright.setText(item.getColumnValue(CinemaItem.PLAYWIGHT).toString());
					textArea_Comment.setText(item.getColumnValue(CinemaItem.COMMENT).toString());
					comboBox_Genre.setSelectedItem(item.getColumnValue(CinemaItem.GENRE));
					slider_Review.setValue(Integer.valueOf(item.getColumnValue(CinemaItem.REVIEW).toString()));
					String[] days = item.getColumnValue(CinemaItem.DATE).toString().split(SEPARATE);
					spinner_Year.setValue(Integer.valueOf(days[0]));
					spinner_Month.setValue(Integer.valueOf(days[1]));
					spinner_Date.setValue(Integer.valueOf(days[2]));
					image = (BufferedImage)item.getColumnValue(CinemaItem.IMAGE);
					BufferedImage resizeImage = converter.resizeImage(image, label_ImageView.getWidth(),label_ImageView.getHeight());
					ImageIcon imageIcon = new ImageIcon(resizeImage);
					label_ImageView.setIcon(imageIcon);
					
					button_Edit.setEnabled(true);
					button_Delete.setEnabled(true);
					button_Add.setEnabled(true);
					frame.setTitle(APPNAME + "- " + tableModel.getValueAt(selectRowIndex, 1));
				}
				break;
			}
			case SaveEdit: { //�ҏW�����A�C�e�����㏑���ۑ�����
				IUserItem editedItem = getItemIncludedComponentValue();
				if(isFulledItemValue(editedItem) == true) {
					cinemaBook.set(idValue, editedItem);
					tableModel.setValueAt(cinemaBook.getId(editedItem), selectRowIndex, 0);
					tableModel.setValueAt(editedItem.getColumnValue(CinemaItem.TITLE), selectRowIndex, 1);
					tableModel.setValueAt(editedItem.getColumnValue(CinemaItem.DATE), selectRowIndex, 2);
					
					button_Add.setEnabled(true);
					button_Delete.setEnabled(true);
					button_Edit.setEnabled(true);
					table.setEnabled(true);
					operationModeChange(OperationMode.View);
				}
				else {
					JOptionPane.showMessageDialog(frame, "���͂���Ă��Ȃ����ڂ�����܂�","",JOptionPane.WARNING_MESSAGE);
					operationModeChange(OperationMode.Edit);
				}
				break;
			}
			case SaveNewItem: { //�ǉ����[�h�œ��͂����A�C�e����V�K�ۑ�����
				IUserItem item = getItemIncludedComponentValue();
				if(isFulledItemValue(item) == true) {
					cinemaBook.addItem(item);
					tableModel.addRow(new Object[]{cinemaBook.getIndex(),item.getColumnValue(CinemaItem.TITLE),item.getColumnValue(CinemaItem.DATE)});
					operationModeChange(OperationMode.View);
				}
				else {
					JOptionPane.showMessageDialog(frame, "���͂���Ă��Ȃ����ڂ�����܂�","",JOptionPane.WARNING_MESSAGE);
					operationModeChange(OperationMode.Add);
				}
				break;
			}
			case Search: {
				List<Integer> idList = new LinkedList<>();
				String searchWords = JOptionPane.showInputDialog(frame, "�������[�h����͂��Ă�������");
				if(searchWords == null || searchWords.length() == 0){
					operationModeChange(OperationMode.View);
					return;
				}
				for(int index = 0; index < tableModel.getRowCount(); index++){
					int id = Integer.valueOf(tableModel.getValueAt(index, 0).toString());
					IUserItem item = cinemaBook.getItem(id);
					for(int columnIndex = 0; columnIndex < item.getColumnIndex(); columnIndex++){
						if(item.getColumnName(columnIndex).toString().equals(CinemaItem.IMAGE) || item.getColumnName(columnIndex).toString().equals(CinemaItem.COMMENT) ||
								item.getColumnName(columnIndex).toString().equals(CinemaItem.DATE)){
							//Image�ƃR�����g�ƋL�^���͌������Ȃ�
							continue;
						}
						if(item.getColumnValue(columnIndex).toString().equals(searchWords) || item.getColumnValue(columnIndex).toString().contains(searchWords)){
							idList.add(id);
							break;
						}
					}
				}
				
				tableModel.setRowCount(0);
				for(int idListIndex = 0; idListIndex < idList.size(); idListIndex++){
					int id = idList.get(idListIndex);
					IUserItem item = cinemaBook.getItem(id);
					tableModel.addRow(new Object[]{id,item.getColumnValue(CinemaItem.TITLE),item.getColumnValue(CinemaItem.DATE)});
				}
				JOptionPane.showMessageDialog(frame, idList.size() + "���̃A�C�e����������܂���");
				frame.setTitle(APPNAME + "�������[�h");
			}
		}
	}

	private void initialize() { //���ׂẴR���|�[�l���g�̏�����
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 808, 718);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel_Operation = new JPanel();
		panel_Operation.setBackground(SystemColor.inactiveCaptionBorder);
		panel_Operation.setBounds(0, 0, 802, 40);
		frame.getContentPane().add(panel_Operation);
		panel_Operation.setLayout(null);
		
		button_Add = new JButton("�A�C�e���̒ǉ�");
		button_Add.setFont(LABEl_FONT);
		button_Add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				operationModeChange(OperationMode.Add);
			}
		});
		button_Add.setBounds(12, 10, 124, 21);
		panel_Operation.add(button_Add);
		
		button_Delete = new JButton("�A�C�e���̍폜");
		button_Delete.setFont(LABEl_FONT);
		button_Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				operationModeChange(OperationMode.Delete);
			}
		});
		button_Delete.setBounds(426, 10, 124, 21);
		panel_Operation.add(button_Delete);
		
		button_Edit = new JButton("�A�C�e���̕ҏW");
		button_Edit.setFont(LABEl_FONT);
		button_Edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				operationModeChange(OperationMode.Edit);
			}
		});
		button_Edit.setBounds(291, 10, 124, 21);
		panel_Operation.add(button_Edit);
		
		button_Save = new JButton("�A�C�e���̕ۑ�");
		button_Save.setFont(LABEl_FONT);
		button_Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(operation == OperationMode.Edit)	{
					operationModeChange(OperationMode.SaveEdit);
				}
				else if(operation == OperationMode.Add)	{
					operationModeChange(OperationMode.SaveNewItem);
				}
			}
		});
		button_Save.setBounds(148, 10, 131, 21);
		panel_Operation.add(button_Save);
		
		JPanel panel_Viewer = new JPanel();
		panel_Viewer.setBackground(SystemColor.textInactiveText);
		panel_Viewer.setBounds(0, 39, 319, 619);
		frame.getContentPane().add(panel_Viewer);
		panel_Viewer.setLayout(new BorderLayout(0, 0));
		
		JLabel label_TitleList = new JLabel("�^�C�g�����X�g");
		label_TitleList.setFont(LABEl_FONT);
		label_TitleList.setForeground(LABEL_FORECOLOR);
		label_TitleList.setBackground(Color.WHITE);
		label_TitleList.setHorizontalAlignment(SwingConstants.CENTER);
		panel_Viewer.add(label_TitleList, BorderLayout.NORTH);
		
		JScrollPane scrollPane_Table = new JScrollPane();
		panel_Viewer.add(scrollPane_Table, BorderLayout.CENTER);
		
		table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		DefaultTableColumnModel columnModel = (DefaultTableColumnModel)table.getColumnModel();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				operationModeChange(OperationMode.Select);
			}
		});
		scrollPane_Table.setViewportView(table);
		columnModel.getColumn(0).setPreferredWidth(20);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(2).setPreferredWidth(100);
		
		JPanel panel_Edit = new JPanel();
		panel_Edit.setBackground(SystemColor.textInactiveText);
		panel_Edit.setBounds(318, 39, 484, 619);
		frame.getContentPane().add(panel_Edit);
		panel_Edit.setLayout(null);
		
		JLabel label_Title = new JLabel("�^�C�g��:");
		label_Title.setFont(LABEl_FONT);
		label_Title.setForeground(LABEL_FORECOLOR);
		label_Title.setBounds(12, 26, 77, 13);
		panel_Edit.add(label_Title);
		
		textField_Title = new JTextField();
		textField_Title.setBounds(68, 23, 242, 19);
		panel_Edit.add(textField_Title);
		textField_Title.setColumns(10);
		
		JLabel label_Director = new JLabel("�ē�:");
		label_Director.setFont(LABEl_FONT);
		label_Director.setForeground(LABEL_FORECOLOR);
		label_Director.setBounds(12, 63, 50, 13);
		panel_Edit.add(label_Director);
		
		textField_Director = new JTextField();
		textField_Director.setBounds(68, 60, 242, 19);
		panel_Edit.add(textField_Director);
		textField_Director.setColumns(10);
		
		JLabel label_Actor = new JLabel("�o�D:");
		label_Actor.setFont(LABEl_FONT);
		label_Actor.setForeground(LABEL_FORECOLOR);
		label_Actor.setBounds(12, 144, 50, 13);
		panel_Edit.add(label_Actor);
		
		textField_Actor = new JTextField();
		textField_Actor.setBounds(68, 141, 242, 19);
		panel_Edit.add(textField_Actor);
		textField_Actor.setColumns(10);
		
		JLabel label_Comment = new JLabel("�R�����g:");
		label_Comment.setFont(LABEl_FONT);
		label_Comment.setForeground(LABEL_FORECOLOR);
		label_Comment.setBounds(12, 348, 77, 13);
		panel_Edit.add(label_Comment);
		
		JScrollPane scrollPane_Comment = new JScrollPane();
		scrollPane_Comment.setBounds(12, 373, 460, 236);
		panel_Edit.add(scrollPane_Comment);
		
		textArea_Comment = new JTextArea();
		scrollPane_Comment.setViewportView(textArea_Comment);
		
		JLabel label_Genre = new JLabel("�W������:");
		label_Genre.setFont(LABEl_FONT);
		label_Genre.setForeground(LABEL_FORECOLOR);
		label_Genre.setBounds(12, 187, 77, 13);
		panel_Edit.add(label_Genre);
		
		comboBox_Genre = new JComboBox(GENRELIST);
		comboBox_Genre.setFont(LABEl_FONT);
		comboBox_Genre.setBounds(68, 184, 162, 19);
		panel_Edit.add(comboBox_Genre);
		
		label_Review = new JLabel("�]��:");
		label_Review.setFont(LABEl_FONT);
		label_Review.setForeground(LABEL_FORECOLOR);
		label_Review.setBounds(12, 241, 50, 13);
		panel_Edit.add(label_Review);
		
		slider_Review = new JSlider();
		slider_Review.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				label_Review.setText("�]��: " + slider_Review.getValue());
			}
		});
		slider_Review.setForeground(Color.WHITE);
		slider_Review.setPaintLabels(true);
		slider_Review.setLabelTable(slider_Review.createStandardLabels(1));
		slider_Review.setSnapToTicks(true);
		slider_Review.setPaintTicks(true);
		slider_Review.setValue(1);
		slider_Review.setMinorTickSpacing(1);
		slider_Review.setMinimum(1);
		slider_Review.setMaximum(10);
		slider_Review.setBackground(SystemColor.textInactiveText);
		slider_Review.setBounds(68, 222, 242, 49);
		panel_Edit.add(slider_Review);
		
		JLabel label_Playwright = new JLabel("�r�{��:");
		label_Playwright.setFont(LABEl_FONT);
		label_Playwright.setForeground(LABEL_FORECOLOR);
		label_Playwright.setBounds(12, 102, 50, 13);
		panel_Edit.add(label_Playwright);
		
		textField_Playwright = new JTextField();
		textField_Playwright.setBounds(68, 99, 242, 19);
		panel_Edit.add(textField_Playwright);
		textField_Playwright.setColumns(10);
		
		JLabel label_RecordDate = new JLabel("�L�^��:");
		label_RecordDate.setFont(LABEl_FONT);
		label_RecordDate.setForeground(LABEL_FORECOLOR);
		label_RecordDate.setBounds(12, 281, 50, 13);
		panel_Edit.add(label_RecordDate);
		
		spinner_Year = new JSpinner();
		spinner_Year.setModel(new SpinnerNumberModel(2015, 1900, 2100, 1));
		spinner_Year.setBounds(12, 304, 50, 20);
		panel_Edit.add(spinner_Year);
		
		JLabel label_Year = new JLabel("�N");
		label_Year.setFont(LABEl_FONT);
		label_Year.setForeground(LABEL_FORECOLOR);
		label_Year.setBounds(68, 307, 50, 13);
		panel_Edit.add(label_Year);
		
		spinner_Month = new JSpinner();
		spinner_Month.setModel(new SpinnerNumberModel(1, 1, 12, 1));
		spinner_Month.setBounds(91, 304, 50, 20);
		panel_Edit.add(spinner_Month);
		
		JLabel label_Month = new JLabel("��");
		label_Month.setFont(LABEl_FONT);
		label_Month.setForeground(LABEL_FORECOLOR);
		label_Month.setBounds(153, 307, 50, 13);
		panel_Edit.add(label_Month);
		
		spinner_Date = new JSpinner();
		spinner_Date.setModel(new SpinnerNumberModel(1, 1, 31, 1));
		spinner_Date.setBounds(177, 304, 50, 20);
		panel_Edit.add(spinner_Date);
		
		JLabel label_Date = new JLabel("��");
		label_Date.setFont(LABEl_FONT);
		label_Date.setForeground(LABEL_FORECOLOR);
		label_Date.setBounds(235, 307, 50, 13);
		panel_Edit.add(label_Date);
		
		label_ImageView = new JLabel("�N���b�N���Ă�������");
		label_ImageView.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				FileDialog openFileDialog = new FileDialog(frame, "�摜��I�����Ă�������", FileDialog.LOAD);
				openFileDialog.setVisible(true);
				File file = new File(openFileDialog.getDirectory() + File.separatorChar + openFileDialog.getFile());
				try {
					image = ImageIO.read(file);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "�I�������t�@�C�����s���ł�","",JOptionPane.ERROR_MESSAGE);
					image = null;
					return;
				}
				
				if(image instanceof BufferedImage){
					image = converter.resizeImage(image, 500, 500); //�t�@�C���ۑ����̗e�ʂ�}���邽�߂ɏk������
					BufferedImage resizeImage = image;
					resizeImage = converter.resizeImage(resizeImage, label_ImageView.getWidth(),label_ImageView.getHeight());
					ImageIcon imageIcon = new ImageIcon(resizeImage);
					label_ImageView.setIcon(imageIcon);
				}else{
					JOptionPane.showMessageDialog(frame, "�I�������t�@�C�����s���ł�","",JOptionPane.ERROR_MESSAGE);
					image = null;
					return;
				}
			}
		});
		label_ImageView.setFont(LABEl_FONT);
		label_ImageView.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		label_ImageView.setForeground(LABEL_FORECOLOR);
		label_ImageView.setBackground(Color.BLACK);
		label_ImageView.setBounds(336, 14, 136, 188);
		panel_Edit.add(label_ImageView);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menu_File = new JMenu("�t�@�C��");
		menu_File.setFont(new Font("�l�r �o�S�V�b�N", Font.BOLD, 14));
		menuBar.add(menu_File);
		
		JMenuItem menuItem_OpenFile = new JMenuItem("�J��...");
		menuItem_OpenFile.setFont(MENUITEM_FONT);
		menuItem_OpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileDialog openFileDialog = new FileDialog(frame, "�J��", FileDialog.LOAD);
				openFileDialog.setFile("*.xml");
				openFileDialog.setVisible(true);
				String fileName = openFileDialog.getDirectory() + File.separatorChar + openFileDialog.getFile();
				File file = new File(fileName);
				
				try{
					cinemaBook.readXml(file);
					for(int i = 0; i < cinemaBook.size(); i++) {
						IUserItem item = (CinemaItem) cinemaBook.indexOf(i);
						int idValue = cinemaBook.getId(item);
						tableModel.addRow(new Object[]{idValue,item.getColumnValue(CinemaItem.TITLE),item.getColumnValue(CinemaItem.DATE)});
					}
				}catch(Exception exception){
					JOptionPane.showMessageDialog(frame, "�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B","",JOptionPane.ERROR_MESSAGE);
					exception.printStackTrace();
				}
				filePath = file;
			}
		});
		menu_File.add(menuItem_OpenFile);
		
		JMenuItem menuItem_Save = new JMenuItem("�㏑���ۑ�");
		menuItem_Save.setFont(MENUITEM_FONT);
		menuItem_Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filePath.exists())
				{
					try{
						cinemaBook.writeXml(filePath);
					}catch(Exception exception){
						JOptionPane.showMessageDialog(frame, "�t�@�C���̏������݂Ɏ��s���܂����B","",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		menu_File.add(menuItem_Save);
		
		JMenuItem menuItem_SaveAs = new JMenuItem("���O��t���ĕۑ�...");
		menuItem_SaveAs.setFont(MENUITEM_FONT);
		menuItem_SaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog saveFileDialog = new FileDialog(frame, "���O��t���ĕۑ�", FileDialog.SAVE);
				saveFileDialog.setFile("*.xml");
				saveFileDialog.setVisible(true);
				String fileName = saveFileDialog.getDirectory() + File.separatorChar + saveFileDialog.getFile();
				File file = new File(fileName);
				
				try{
					cinemaBook.writeXml(file);
				}catch(Exception exception){
					JOptionPane.showMessageDialog(frame, "�t�@�C���̏������݂Ɏ��s���܂����B","",JOptionPane.ERROR_MESSAGE);
				}
				filePath = file;
			}
		});
		menu_File.addSeparator();
		
		JMenuItem menuItem�QClose = new JMenuItem("����");
		menuItem�QClose.setFont(MENUITEM_FONT);
		menuItem�QClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(JOptionPane.showConfirmDialog(frame, "�����邷�ׂẴf�[�^���������܂�") == JOptionPane.YES_OPTION){
					initializeComponentValue();
					tableModel.setRowCount(0);
					cinemaBook.clearItems();
					filePath = new File("");
					operationModeChange(OperationMode.View);
				}
			}
		});
		menu_File.add(menuItem�QClose);
		menu_File.addSeparator();
		menu_File.add(menuItem_SaveAs);
		menu_File.addSeparator();
		
		JMenuItem menuItem_Exit = new JMenuItem("�I��");
		menuItem_Exit.setFont(MENUITEM_FONT);
		menuItem_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(JOptionPane.showConfirmDialog(frame, "�I�����܂�") == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});
		menuItem_Exit.setFont(MENUITEM_FONT);
		menu_File.add(menuItem_Exit);
		
		JMenu menu_Search = new JMenu("�c�[��");
		menu_Search.setFont(new Font("�l�r �o�S�V�b�N", Font.BOLD, 14));
		menuBar.add(menu_Search);
		
		JMenuItem menuItem_Search = new JMenuItem("����");
		menuItem_Search.setFont(MENUITEM_FONT);
		menuItem_Search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				operationModeChange(OperationMode.Search);
			}
		});
		menu_Search.add(menuItem_Search);
		
		JMenuItem menuItem_EndSearchMode = new JMenuItem("�������[�h�I��");
		menuItem_EndSearchMode.setFont(MENUITEM_FONT);
		menuItem_EndSearchMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tableModel.setRowCount(0);
				for(int index = 0; index < cinemaBook.size(); index++){
					IUserItem item = cinemaBook.indexOf(index);
					int id = cinemaBook.getId(item);
					tableModel.addRow(new Object[]{id,item.getColumnValue(CinemaItem.TITLE),item.getColumnValue(CinemaItem.DATE)});
				}
				initializeComponentValue();
				operationModeChange(OperationMode.View);
			}
		});
		menu_Search.add(menuItem_EndSearchMode);
	}
	
	public Form() {
		image = null;
		tableModel = new DefaultTableModel(COLUMNNAMES, 0);
		cinemaBook = new CinemaBook();
		converter = Converter.getInstance();
		filePath = new File("");
		initialize();
		operationModeChange(OperationMode.View);
	}
}