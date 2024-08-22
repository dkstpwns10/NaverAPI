import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chzzk.temtem.utils.SettingsRepo
import com.chzzk.temtem.utils.subscribeTopics
import com.chzzk.temtem.utils.unsubscribeTopic
import kotlinx.coroutines.launch


@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    settingsRepository: SettingsRepo
) {
    var showNotificationSettings by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("설정", fontWeight = FontWeight.Bold, fontSize = 25.sp)
                },
        text = {
            if (showNotificationSettings=="Alert") {
                NotificationSettingsContent(settingsRepository)
            } else {
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Spacer(modifier = Modifier.size(16.dp))
                    TextButton(modifier = Modifier.padding(8.dp), onClick = { showNotificationSettings = "Alert" }) {
                        Text("알림 설정", fontWeight = FontWeight.Bold)
                    }
                    Divider()
                    TextButton(modifier = Modifier.padding(8.dp),onClick = { /* 다크모드 토글 로직 */ }) {
                        Text("다크모드", fontWeight = FontWeight.Bold)
                    }
                    Divider()
                    TextButton(modifier = Modifier.padding(8.dp),onClick = {/* 회원탈퇴 로직 */  }) {
                        Text("회원탈퇴", fontWeight = FontWeight.Bold)
                    }
                    Divider()
                    TextButton(modifier = Modifier.padding(8.dp),onClick = { /* 개발자 정보 표시 로직 */ }) {
                        Text("개발자 정보", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        }
    )
}

@Composable
fun NotificationSettingsContent(settingsRepository: SettingsRepo) {

    val broadcastEnabled by settingsRepository.broadcastNotificationFlow.collectAsState(initial = true)
    val teamEnabled by settingsRepository.teamNotificationFlow.collectAsState(initial = true)
    val teamitterEnabled by settingsRepository.teamitterNotificationFlow.collectAsState(initial = true)
    val noticeEnabled by settingsRepository.noticeNotificationFlow.collectAsState(initial = true)

    val coroutineScope = rememberCoroutineScope()

    Column {
        NotificationSettingItem("방송 알림", broadcastEnabled) { isEnabled ->
            coroutineScope.launch {
                settingsRepository.setBroadcastNotification(isEnabled)
                if(settingsRepository.getNotificationState("broadcast_alert")){
                    subscribeTopics("broadcast_alert")
                }else{
                    unsubscribeTopic("broadcast_alert")
                }
            }
        }
        NotificationSettingItem("탬 알림", teamEnabled) { isEnabled ->
            coroutineScope.launch {
                settingsRepository.setTeamNotification(isEnabled)
                if(settingsRepository.getNotificationState("tem_alarm")){
                    subscribeTopics("tem_alarm")
                }else{
                    unsubscribeTopic("tem_alarm")
                }
            }
        }
        NotificationSettingItem("탬 위터", teamitterEnabled) { isEnabled ->
            coroutineScope.launch {
                settingsRepository.setTeamitterNotification(isEnabled)
                if(settingsRepository.getNotificationState("tem_twitter")){
                    subscribeTopics("tem_twitter")
                }else{
                    unsubscribeTopic("tem_twitter")
                }
            }
        }
        NotificationSettingItem("공지 알림", noticeEnabled) { isEnabled ->
            coroutineScope.launch {
                settingsRepository.setNoticeNotification(isEnabled)
                if(settingsRepository.getNotificationState("notice")){
                    subscribeTopics("notice")
                }else{
                    unsubscribeTopic("notice")
                }
            }
        }
    }
}

@Composable
fun NotificationSettingItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}